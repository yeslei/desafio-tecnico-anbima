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
    public ResponseEntity<Pedido> receberPedidoPosicional(@RequestBody String linhaPosicional) {
        
        if (linhaPosicional == null || linhaPosicional.length() != 40) {
            return ResponseEntity.badRequest().build();
        }

        Pedido pedidoSalvo = pedidoService.processarEGravarPedido(linhaPosicional);
        
        return new ResponseEntity<>(pedidoSalvo, HttpStatus.CREATED);
    }
}