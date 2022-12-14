package com.example.orderservice.jpa;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    OrderEntity findByProductId(String productId);

    OrderEntity findByOrderId(String orderId);

    Iterable<OrderEntity> findByUserId(String userId);
}
