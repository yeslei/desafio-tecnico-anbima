package com.anbima.modulo_a_gateway.service;

import com.anbima.modulo_a_gateway.model.Pedido;
import com.anbima.modulo_a_gateway.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
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

        // 4. Verificação da Regra de Desconto (10%)
        // Regra: Hamburguer + Carne + Salada
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

        return repository.save(p);
    }
}