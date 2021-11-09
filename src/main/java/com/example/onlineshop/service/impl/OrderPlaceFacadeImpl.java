package com.example.onlineshop.service.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.ProductDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.service.OrderItemService;
import com.example.onlineshop.service.OrderPlaceFacade;
import com.example.onlineshop.service.OrderService;
import com.example.onlineshop.service.ProductService;
import com.example.onlineshop.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderPlaceFacadeImpl implements OrderPlaceFacade {

	private final OrderService orderService;

	private final OrderItemService orderItemService;

	private final ProductService productService;

	private final UserService userUservice;

	@Override
	public OrderDto placeNewEmptyOrder(Long userId) {
		return orderService.placeNewEmptyOrder(userId);
	}

	@Override
	public OrderItemDto addItemToOrder(ItemFormDto itemForm, Long orderId) {
		return orderItemService.addItemToOrder(itemForm, orderId);
	}

	@Override
	public OrderDto mergeItemToCurrentOrder(ItemFormDto itemForm, Long orderId) {
		return null;
	}

	@Override
	public OrderItemDto modifyItemQuantityOfOrder(Long userId, Long orderItemId, Integer newQuantity) {
		return orderItemService.modifyItemQuantityOfOrder(userId, orderItemId, newQuantity);
	}

	@Override
	public void removeOrderItem(Long userId, Long orderItemId) {
		orderItemService.removeOrderItem(userId, orderItemId);

	}

	@Override
	public OrderDto checkoutTheOrder(OrderDto orderDto) {
		return orderService.checkoutOrder(orderDto);
	}

	@Override
	public OrderDto getOrderbyId(Long userId, Long orderId) {
		return orderService.getOrderById(userId, orderId);
	}

	@Override
	public ProductDto getProductById(Long productId) {
		return null;
	}

	@Override
	public PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria) {
		return orderService.findAllOrders(pageRequest, criteria);
	}

}
