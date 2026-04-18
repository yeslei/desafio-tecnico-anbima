package com.anbima.modulo_a_gateway.service;

import com.anbima.modulo_a_gateway.model.Pedido;
import com.anbima.modulo_a_gateway.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private RabbitService rabbitService;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processarEGravarPedido_ComDesconto_DeveCalcularCorretamente() {

        String tipoLanche =     "HAMBURGUER"; // 10 chars
        String proteina =       "CARNE     "; // 10 chars
        String acompanhamento = "SALADA    "; // 10 chars
        String quantidade =     "01";         // 2 chars
        String bebida =         "COCA    ";   // 8 chars
        
        String linhaPosicional = tipoLanche + proteina + acompanhamento + quantidade + bebida;

        when(repository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        Pedido resultado = pedidoService.processarEGravarPedido(linhaPosicional);

        assertNotNull(resultado);
        assertEquals("HAMBURGUER", resultado.getTipoLanche());
        assertEquals(new BigDecimal("18.00"), resultado.getValor());
        assertEquals("RECEBIDO", resultado.getStatus());

        verify(repository, times(1)).save(any(Pedido.class));
        verify(rabbitService, times(1)).enviarMensagemPedidoCriado(1L);
    }

    @Test
    void processarEGravarPedido_SemDesconto_DeveCalcularCorretamente() {
   
        String tipoLanche =     "PASTEL    "; // 10 chars
        String proteina =       "FRANGO    "; // 10 chars
        String acompanhamento = "BACON     "; // 10 chars
        String quantidade =     "02";         // 2 chars
        String bebida =         "SUCO    ";   // 8 chars
        
        String linhaPosicional = tipoLanche + proteina + acompanhamento + quantidade + bebida;

        when(repository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        Pedido resultado = pedidoService.processarEGravarPedido(linhaPosicional);

        assertNotNull(resultado);
        assertEquals("PASTEL", resultado.getTipoLanche().trim());
        assertEquals(2, resultado.getQuantidade());
        assertEquals(new BigDecimal("30.00"), resultado.getValor());
        
        verify(repository, times(1)).save(any(Pedido.class));
    }

    @Test
    void processarEGravarPedido_QuantidadeInvalida_DeveLancarExcecao() {

        String tipoLanche =     "PASTEL    "; // 10 chars
        String proteina =       "FRANGO    "; // 10 chars
        String acompanhamento = "BACON     "; // 10 chars
        String quantidade =     "XX";         // 2 chars (INVÁLIDO)
        String bebida =         "SUCO    ";   // 8 chars
        
        String linhaPosicional = tipoLanche + proteina + acompanhamento + quantidade + bebida;

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> pedidoService.processarEGravarPedido(linhaPosicional)
        );

        assertEquals("A quantidade deve ser numérica.", exception.getMessage());
        
        verify(repository, never()).save(any());
        verify(rabbitService, never()).enviarMensagemPedidoCriado(any());
    }
}