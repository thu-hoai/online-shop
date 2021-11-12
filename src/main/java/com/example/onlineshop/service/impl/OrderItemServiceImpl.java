package com.example.onlineshop.service.impl;

import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.entity.OrderItem;
import com.example.onlineshop.mapper.IOrderItemMapper;
import com.example.onlineshop.repository.OrderItemRepository;
import com.example.onlineshop.service.OrderItemService;
import com.example.onlineshop.service.exception.OrderItemNotFoundException;
import com.example.onlineshop.utils.AuthUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

	private final OrderItemRepository orderItemRepository;

	private final IOrderItemMapper orderItemMapper;

	@Override
	public OrderItemDto modifyItemQuantityOfOrder(Long userId, Long orderItemId, Integer newQuantity) {

		AuthUtils.getAuthorizedUser(userId);
		OrderItem item = orderItemRepository.findById(orderItemId).orElseThrow(
				() -> new OrderItemNotFoundException(String.format("Order item with id %d not found", orderItemId)));

		item.setQuantity(newQuantity);
		OrderItem modifiedItem = orderItemRepository.save(item);
		return orderItemMapper.convertToItemDto(modifiedItem);
	}

	@Override
	public void removeOrderItem(Long userId, Long orderItemId) {
		AuthUtils.getAuthorizedUser(userId);
		OrderItem item = orderItemRepository.findById(orderItemId).orElseThrow(
				() -> new OrderItemNotFoundException(String.format("Order item with id %d not found", orderItemId)));
		orderItemRepository.deleteById(item.getIds().getOrderId());
	}

}
