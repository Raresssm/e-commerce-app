package com.rares.ecommerce.order.OrderLine.Service;

import com.rares.ecommerce.order.Order.DTO.OrderResponse;
import com.rares.ecommerce.order.OrderLine.DTO.OrderLineRequest;
import com.rares.ecommerce.order.OrderLine.DTO.OrderLineResponse;
import com.rares.ecommerce.order.OrderLine.Mapper.OrderLineMapper;
import com.rares.ecommerce.order.OrderLine.Repository.OrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.Hibernate.map;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;
    public Integer saveOrderLine(OrderLineRequest request) {
        var order = mapper.toOrderLine(request);
        return repository.save(order).getId();
    }

    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
                .stream()
                .map(mapper::toOrderLineResponse)
                .collect(Collectors.toList());
    }
}
