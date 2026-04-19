package com.anbima.modulo_b_gateway.controller;

import com.anbima.modulo_b_gateway.model.Pedido;
import com.anbima.modulo_b_gateway.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoControllerTest {

    @Mock
    private PedidoService service;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_DeveRetornarListaDePedidos() {
        Pedido pedido = new Pedido();
        pedido.setId(10L);
        pedido.setStatus("ENTREGUE");

        when(service.listarTodos()).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoController.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ENTREGUE", resultado.get(0).getStatus());

        verify(service, times(1)).listarTodos();
    }

    @Test
    void buscar_DeveRetornarPedidoEStatus200SeExistir() {
        Pedido pedido = new Pedido();
        pedido.setId(5L);

        when(service.buscarPorId(5L)).thenReturn(Optional.of(pedido));

        // Aqui está a correção: chamando apenas buscar()
        ResponseEntity<Pedido> resultado = pedidoController.buscar(5L);

        assertNotNull(resultado);
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(5L, resultado.getBody().getId());

        verify(service, times(1)).buscarPorId(5L);
    }

    @Test
    void buscar_DeveRetornarStatus404SeNaoExistir() {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        // Aqui está a correção: chamando apenas buscar()
        ResponseEntity<Pedido> resultado = pedidoController.buscar(99L);

        assertNotNull(resultado);
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertNull(resultado.getBody());

        verify(service, times(1)).buscarPorId(99L);
    }
}