package com.example.onlineshop.repository;

import com.example.onlineshop.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.onlineshop.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

  List<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
