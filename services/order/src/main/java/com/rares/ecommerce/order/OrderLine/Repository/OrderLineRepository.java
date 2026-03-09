package com.rares.ecommerce.order.OrderLine.Repository;

import com.rares.ecommerce.order.Order.DTO.OrderResponse;
import com.rares.ecommerce.order.OrderLine.Model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
    List<OrderLine> findAllByOrderId(Integer orderId);
}
