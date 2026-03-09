package com.rares.ecommerce.order.OrderLine.Repository;

import com.rares.ecommerce.order.OrderLine.Model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
}
