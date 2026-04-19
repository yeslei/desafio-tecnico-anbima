package com.anbima.modulo_b_gateway.listener;

import com.anbima.modulo_b_gateway.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PedidoListenerTest {

    @Mock
    private PedidoRepository repository;

    @InjectMocks
    private PedidoListener pedidoListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processarPedidoRecebido_ComJsonValido_DeveAtualizarBanco() {

        String jsonValido = "{\"pedidoId\": 1}";

        when(repository.atualizarStatusParaEntregue(1L)).thenReturn(1);

        pedidoListener.processarPedidoRecebido(jsonValido);

        verify(repository, times(1)).atualizarStatusParaEntregue(1L);
    }

    @Test
    void processarPedidoRecebido_ComJsonInvalido_NaoDevesAtualizarBanco() {

        String jsonInvalido = "{\"id_errado\": 99}";


        pedidoListener.processarPedidoRecebido(jsonInvalido);

        verify(repository, never()).atualizarStatusParaEntregue(anyLong());
    }
}