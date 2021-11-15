package com.example.onlineshop.service.impl;

import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.entity.OrderItem;
import com.example.onlineshop.enums.ExceptionCode;
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
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messageErrorResouce");

	@Override
	public OrderItemDto modifyItemQuantityOfOrder(Long userId, Long orderItemId, Integer newQuantity) {

		AuthUtils.getAuthorizedUser(userId);
		OrderItem item = orderItemRepository.findById(orderItemId).orElseThrow(() -> new OrderItemNotFoundException(
				getErrorMessage(ExceptionCode.ORDER_ITEM_NOT_FOUND, orderItemId.toString())));

		item.setQuantity(newQuantity);
		OrderItem modifiedItem = orderItemRepository.save(item);
		return orderItemMapper.convertToItemDto(modifiedItem);
	}

	@Override
	public void removeOrderItem(Long userId, Long orderItemId) {
		AuthUtils.getAuthorizedUser(userId);
		OrderItem item = orderItemRepository.findById(orderItemId).orElseThrow(() -> new OrderItemNotFoundException(
				getErrorMessage(ExceptionCode.ORDER_ITEM_NOT_FOUND, orderItemId.toString())));
		orderItemRepository.deleteById(item.getIds().getOrderId());
	}

	private String getErrorMessage(ExceptionCode messageKey, String arg) {
		return String.format(resourceBundle.getString(messageKey.toString()), arg);
	}

}
