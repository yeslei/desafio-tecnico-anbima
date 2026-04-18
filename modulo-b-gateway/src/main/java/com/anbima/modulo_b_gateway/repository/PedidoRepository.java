package com.anbima.modulo_b_gateway.repository;

import com.anbima.modulo_b_gateway.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Pedido p SET p.status = 'ENTREGUE' WHERE p.id = :id")
    int atualizarStatusParaEntregue(@Param("id") Long id);
}