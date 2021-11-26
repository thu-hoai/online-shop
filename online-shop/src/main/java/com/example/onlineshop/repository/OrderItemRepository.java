package com.example.onlineshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineshop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	
	List<OrderItem> findByIdsOrderIdAndIdsProductId(Long orderId, Long productId);

}
