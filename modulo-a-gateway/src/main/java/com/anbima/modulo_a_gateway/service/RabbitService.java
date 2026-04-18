package com.anbima.modulo_a_gateway.service;

import com.anbima.modulo_a_gateway.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    private final RabbitTemplate rabbitTemplate;

    public RabbitService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void enviarMensagemPedidoCriado(Long pedidoId) {
        try {
            String jsonMensagem = String.format("{\"pedidoId\": %d}", pedidoId);
            rabbitTemplate.convertAndSend(RabbitConfig.FILA_PEDIDO_CRIADO, jsonMensagem);
            System.out.println("Enviado para fila em background: " + pedidoId);
        } catch (Exception e) {
            // Como é assíncrono, logamos o erro para não perder o rastreio
            System.err.println("Erro ao enviar mensagem assíncrona: " + e.getMessage());
        }
    }
}