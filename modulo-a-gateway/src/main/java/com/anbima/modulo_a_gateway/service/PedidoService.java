package com.anbima.modulo_a_gateway.service;

import com.anbima.modulo_a_gateway.config.RabbitConfig;
import com.anbima.modulo_a_gateway.model.Pedido;
import com.anbima.modulo_a_gateway.repository.PedidoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public PedidoService(PedidoRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Pedido processarEGravarPedido(String linhaPosicional) {

        String tipo = linhaPosicional.substring(0, 10).trim();
        String proteina = linhaPosicional.substring(10, 20).trim();
        String acompanhamento = linhaPosicional.substring(20, 30).trim();
        int qtd = Integer.parseInt(linhaPosicional.substring(30, 32).trim());
        String bebida = linhaPosicional.substring(32, 40).trim();

        BigDecimal valorBase;

        if (tipo.equalsIgnoreCase("HAMBURGUER")) {
            valorBase = new BigDecimal("20.00");
        } else if (tipo.equalsIgnoreCase("PASTEL")) {
            valorBase = new BigDecimal("15.00");
        } else {
            valorBase = new BigDecimal("12.00");
        }

        BigDecimal total = valorBase.multiply(new BigDecimal(qtd));

        // Regra: Hamburguer + Carne + Salada = 10% de desconto
        if (tipo.equalsIgnoreCase("HAMBURGUER") &&
            proteina.equalsIgnoreCase("CARNE") &&
            acompanhamento.equalsIgnoreCase("SALADA")) {

            BigDecimal desconto = total.multiply(new BigDecimal("0.10"));
            total = total.subtract(desconto);
        }

        Pedido p = new Pedido();
        p.setTipoLanche(tipo);
        p.setProteina(proteina);
        p.setAcompanhamento(acompanhamento);
        p.setQuantidade(qtd);
        p.setBebida(bebida);
        p.setValor(total);
        p.setStatus("RECEBIDO");

        Pedido pedidoSalvo = repository.save(p);

        try {
            rabbitTemplate.convertAndSend(
                RabbitConfig.FILA_PEDIDO_CRIADO,
                "{\"pedidoId\": " + pedidoSalvo.getId() + "}"
            );
            System.out.println("ID enviado para o RabbitMQ: " + pedidoSalvo.getId());

        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
        }
        return pedidoSalvo;
    }
}