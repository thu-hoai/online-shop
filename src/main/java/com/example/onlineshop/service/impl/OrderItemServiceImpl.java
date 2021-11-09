package com.example.onlineshop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.entity.Order;
import com.example.onlineshop.entity.OrderItem;
import com.example.onlineshop.entity.OrderItemStatus;
import com.example.onlineshop.entity.Product;
import com.example.onlineshop.enums.OrderItemStatusCode;
import com.example.onlineshop.mapper.IOrderItemMapper;
import com.example.onlineshop.repository.OrderItemRepository;
import com.example.onlineshop.repository.OrderRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.service.OrderItemService;
import com.example.onlineshop.service.exception.InsufficientItemOrderException;
import com.example.onlineshop.service.exception.OrderItemNotFoundException;
import com.example.onlineshop.service.exception.OrderNotFoundException;
import com.example.onlineshop.service.exception.ProductNotFoundException;
import com.example.onlineshop.utils.AuthUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

	private final OrderItemRepository orderItemRepository;

	private final OrderRepository orderRepository;

	private final ProductRepository productRepository;

	private final IOrderItemMapper orderMapper;

	@Transactional
	@Override
	public OrderItemDto addItemToOrder(ItemFormDto itemForm, Long orderId) {
		Order orderEntity = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %d not found", orderId)));

		Product productEntity = productRepository.findById(itemForm.getProductId())
				.orElseThrow(() -> new ProductNotFoundException(
						String.format("Product with id %d not found", itemForm.getProductId())));
		if (productEntity.getProductStock() < itemForm.getQuantity()) {
			throw new InsufficientItemOrderException(String.format("Insufficient item %d", itemForm.getProductId()));
		}
		// TODO: check if it update stock and total amount or not
		productEntity.setProductStock(productEntity.getProductStock() - itemForm.getQuantity());
		if (orderEntity.getOrderItem() == null || orderEntity.getOrderItem().isEmpty()) {
			List<OrderItem> items = new ArrayList<>();
//			items.add()
		} else {
			// merge cart
		}
//		orderEntity.setOrderItem(items);
//		orderEntity.setOrderAmount(calculateOrderAmount(orderEntity.getOrderItem()));

		// Save current item
		OrderItem item = OrderItem.builder()
				.orderItemStatus(
						OrderItemStatus.builder().orderItemStatusCode(OrderItemStatusCode.AVL.toString()).build())
				.order(orderEntity).product(productEntity).quantity(itemForm.getQuantity()).build();
		OrderItem createdItem = orderItemRepository.save(item);

		// update the current order
		return orderMapper.convertToItemDto(createdItem);
	}

	@Override
	public OrderItemDto modifyItemQuantityOfOrder(Long userId, Long orderItemId, Integer newQuantity) {

		AuthUtils.getAuthorizedUser(userId);
		OrderItem item = orderItemRepository.findById(orderItemId).orElseThrow(
				() -> new OrderItemNotFoundException(String.format("Order item with id %d not found", orderItemId)));

		item.setQuantity(newQuantity);
		OrderItem modifiedItem = orderItemRepository.save(item);
		return orderMapper.convertToItemDto(modifiedItem);
	}

	@Override
	public void removeOrderItem(Long userId, Long orderItemId) {
		AuthUtils.getAuthorizedUser(userId);
		OrderItem item = orderItemRepository.findById(orderItemId).orElseThrow(
				() -> new OrderItemNotFoundException(String.format("Order item with id %d not found", orderItemId)));
		orderItemRepository.deleteById(item.getId());
	}

//	private BigDecimal calculateOrderAmount(List<OrderItem> orderItem) {
//		return orderItem.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
//				.reduce(BigDecimal.ZERO, BigDecimal::add);
//	}

}
