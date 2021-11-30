package com.example.onlineshop.repository;

import java.util.List;
import java.util.Optional;

import com.example.onlineshop.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineshop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
	
	List<OrderItem> findByIdsOrderIdAndIdsProductId(Long orderId, Long productId);

}
