package com.example.onlineshop.service;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.OrderItemDto;

public interface OrderItemService {

	OrderItemDto modifyItemQuantityOfOrder(Long userId, Long orderItem, Integer newQuantity);
	
	void removeOrderItem(Long userId, Long orderItemId);

	OrderDto addItemToOrder(ItemFormDto itemForm, Long orderId);
}
