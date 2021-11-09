package com.example.onlineshop.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.ProductDto;
import com.example.onlineshop.dto.SearchCriteria;

public interface OrderPlaceFacade {

	/**
	 * Create a new empty order
	 */
	public OrderDto placeNewEmptyOrder(Long userId);

	/**
	 * Add a new item to the specific order
	 * 
	 * @param itemForm
	 * @return
	 */
	public OrderItemDto addItemToOrder(ItemFormDto itemForm, Long orderId);

	public OrderDto mergeItemToCurrentOrder(ItemFormDto itemForm, Long orderId);

	/**
	 * Modify the quantity of the specific item
	 * 
	 * @param userId
	 * @param orderItem
	 * @param newQuantity
	 * @return
	 */
	public OrderItemDto modifyItemQuantityOfOrder(Long userId, Long orderItem, Integer newQuantity);

	/**
	 * Remove the specific item
	 * 
	 * @param userId
	 * @param orderItemId
	 */
	public void removeOrderItem(Long userId, Long orderItemId);

	/**
	 * Get order by order id. Calculate the temp order amount
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public OrderDto getOrderbyId(Long userId, Long orderId);

	public PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria);

	/**
	 * Checkout the shopping cart
	 * 
	 * @param orderDto
	 * @return
	 */
	public OrderDto checkoutTheOrder(OrderDto orderDto);

	public ProductDto getProductById(Long productId);

}
