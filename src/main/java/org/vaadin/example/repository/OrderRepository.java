package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vaadin.example.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
