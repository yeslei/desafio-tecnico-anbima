package com.anbima.modulo_a_gateway.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_lanche", length = 20)
    private String tipoLanche;

    @Column(length = 20)
    private String proteina;

    @Column(length = 20)
    private String acompanhamento;

    private Integer quantidade;

    @Column(length = 20)
    private String bebida;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(length = 20)
    private String status;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;
}