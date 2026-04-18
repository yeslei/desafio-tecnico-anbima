package com.anbima.modulo_a_gateway.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String FILA_PEDIDO_CRIADO = "pedidos.v1.pedido-criado";

    @Bean
    public Queue queue() {
        return new Queue(FILA_PEDIDO_CRIADO, true);
    }
}