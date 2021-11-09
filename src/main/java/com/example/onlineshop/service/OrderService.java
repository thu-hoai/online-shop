package com.example.onlineshop.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.SearchCriteria;

public interface OrderService {

	PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria);

	OrderDto findOrderById(Long id);

	OrderDto placeNewEmptyOrder(Long userId);

	OrderDto changeOrderStatus(Long userId, Long orderId, String newStatus);

	OrderDto checkoutOrder(OrderDto orderDto);
	
	OrderDto getOrderById(Long userId, Long orderId);

}
