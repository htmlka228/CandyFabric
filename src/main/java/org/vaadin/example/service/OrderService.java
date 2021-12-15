package org.vaadin.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Order;
import org.vaadin.example.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
