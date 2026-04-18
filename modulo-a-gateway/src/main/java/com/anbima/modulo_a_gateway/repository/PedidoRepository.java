package com.anbima.modulo_a_gateway.repository;

import com.anbima.modulo_a_gateway.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}