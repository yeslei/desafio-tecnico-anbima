package com.anbima.modulo_b_gateway.listener;

import com.anbima.modulo_b_gateway.repository.PedidoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoListener {

    private final PedidoRepository repository;
    private final ObjectMapper objectMapper;

    public PedidoListener(PedidoRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "pedidos.recebidos")
    public void processarPedidoRecebido(String mensagemJson) {
        try {
            JsonNode jsonNode = objectMapper.readTree(mensagemJson);

            Long id = jsonNode.has("pedidoId")
                    ? jsonNode.get("pedidoId").asLong()
                    : null;

            if (id == null) {
                throw new IllegalArgumentException("ID não encontrado no JSON.");
            }

            int linhasAfetadas = repository.atualizarStatusParaEntregue(id);

            if (linhasAfetadas > 0) {
                System.out.println("Pedido " + id + " atualizado para ENTREGUE.");
            } else {
                System.err.println("Pedido " + id + " não encontrado.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem: " + e.getMessage());
        }
    }
}