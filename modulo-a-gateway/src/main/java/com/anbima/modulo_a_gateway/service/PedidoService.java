package com.anbima.modulo_a_gateway.service;

import com.anbima.modulo_a_gateway.config.RabbitConfig;
import com.anbima.modulo_a_gateway.model.Pedido;
import com.anbima.modulo_a_gateway.repository.PedidoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
        String bebida = linhaPosicional.substring(32, 40).trim();
        
        String qtdStr = linhaPosicional.substring(30, 32).trim();

        validarCampoSemNumeros(tipo, "Tipo de Lanche");
        validarCampoSemNumeros(proteina, "Proteína");
        validarCampoSemNumeros(acompanhamento, "Acompanhamento");
        validarCampoSemNumeros(bebida, "Bebida");
        
        if (qtdStr.length() == 1) {
            qtdStr = "0" + qtdStr;
        } else if (qtdStr.isEmpty()) {
            throw new IllegalArgumentException("A quantidade não pode estar vazia.");
        }
        
        // Valida se possui apenas dígitos
        if (!qtdStr.matches("\\d{2}")) {
            throw new IllegalArgumentException("A quantidade deve ser numérica.");
        }

        int qtd = Integer.parseInt(qtdStr);

        // Regra: Entre 01 e 99
        if (qtd < 1 || qtd > 99) {
            throw new IllegalArgumentException("A quantidade deve estar entre 01 e 99.");
        }

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
            total = total.subtract(desconto).setScale(2, RoundingMode.HALF_UP);
        }

        Pedido p = new Pedido();
        p.setTipoLanche(tipo.toUpperCase());
        p.setProteina(proteina.toUpperCase());
        p.setAcompanhamento(acompanhamento.toUpperCase());
        p.setQuantidade(qtd);
        p.setBebida(bebida.toUpperCase());
        p.setValor(total);
        p.setStatus("RECEBIDO");

        Pedido pedidoSalvo = repository.save(p);

        try {
            String jsonMensagem = String.format("{\"pedidoId\": %d}", pedidoSalvo.getId());
            rabbitTemplate.convertAndSend(RabbitConfig.FILA_PEDIDO_CRIADO, jsonMensagem);
            System.out.println("ID enviado para o RabbitMQ: " + pedidoSalvo.getId());
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
        }
        
        return pedidoSalvo;
    }

    private void validarCampoSemNumeros(String valor, String nomeCampo) {
    // A Regex ".*\\d.*" verifica se existe QUALQUER dígito na string
    if (valor != null && valor.matches(".*\\d.*")) {
        throw new IllegalArgumentException("O campo " + nomeCampo + " não pode conter números: " + valor);
    }
}
}