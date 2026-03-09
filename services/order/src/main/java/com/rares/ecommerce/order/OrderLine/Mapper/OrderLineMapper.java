package com.rares.ecommerce.order.OrderLine.Mapper;

import com.rares.ecommerce.order.Order.Model.Order;
import com.rares.ecommerce.order.OrderLine.DTO.OrderLineResponse;
import com.rares.ecommerce.order.OrderLine.Model.OrderLine;
import com.rares.ecommerce.order.OrderLine.DTO.OrderLineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.id())
                .quantity(request.quantity())
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .productId(request.productId())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity());
    }
}
