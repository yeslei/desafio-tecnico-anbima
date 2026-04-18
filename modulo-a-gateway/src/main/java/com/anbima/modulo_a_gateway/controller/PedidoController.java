package com.anbima.modulo_a_gateway.controller;

import com.anbima.modulo_a_gateway.model.Pedido;
import com.anbima.modulo_a_gateway.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*") 
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping(value = "/posicional", consumes = "text/plain")
    public ResponseEntity<?> receberPedidoPosicional(@RequestBody String linhaPosicional) {
        
        if (linhaPosicional == null || linhaPosicional.length() > 40) {
            return ResponseEntity.badRequest().body("Erro: A linha não pode ter mais que 40 caracteres.");
        }

        // Regra: Se vier com menos de 40, preenchemos com espaços à direita
        // Isso evita o erro de IndexOutOfBounds ao fazer o substring no Service
        if (linhaPosicional.length() < 40) {
            linhaPosicional = String.format("%-40s", linhaPosicional);
        }

        try {
            Pedido pedidoSalvo = pedidoService.processarEGravarPedido(linhaPosicional);
            return new ResponseEntity<>(pedidoSalvo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }
    }
}