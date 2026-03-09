package com.rares.ecommerce.order.Order.Repository;

import com.rares.ecommerce.order.Order.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
