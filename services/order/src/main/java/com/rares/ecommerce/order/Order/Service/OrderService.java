package com.rares.ecommerce.order.Order.Service;

import com.rares.ecommerce.order.Order.DTO.OrderResponse;
import com.rares.ecommerce.order.Clients.Customer.CustomerClient;
import com.rares.ecommerce.order.Order.DTO.OrderRequest;
import com.rares.ecommerce.order.Kafka.OrderConfirmation;
import com.rares.ecommerce.order.Kafka.OrderProducer;
import com.rares.ecommerce.order.Clients.Product.DTO.PurchaseRequest;
import com.rares.ecommerce.order.Exceptions.BusinessException;
import com.rares.ecommerce.order.Order.Mapper.OrderMapper;
import com.rares.ecommerce.order.Order.Repository.OrderRepository;
import com.rares.ecommerce.order.OrderLine.DTO.OrderLineRequest;
import com.rares.ecommerce.order.OrderLine.Service.OrderLineService;
import com.rares.ecommerce.order.Clients.Product.ProductClient;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(@Valid OrderRequest request) {
        var customer = this.customerClient.findCustomerById((request.customerId()))
                .orElseThrow(()->new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        var order = this.repository.save(mapper.toOrder(request));

        for(PurchaseRequest purchaseRequest:request.products()){
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    ));
        }
        //todo start payment process

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return repository.findById(orderId)
                .map(mapper::toOrderResponse)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Cannot find order with ID: %d", orderId)));
    }
}
