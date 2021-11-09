package com.example.onlineshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.entity.OrderItem;

@Mapper
public interface IOrderItemMapper {

	@Mapping(source = "product.productId", target = "productId")
	@Mapping(source = "order.orderId", target = "orderId")
	@Mapping(source = "orderItemStatus.orderItemStatusCode", target = "status")
	@Mapping(source = "product.price", target = "price")
	public OrderItemDto convertToItemDto(OrderItem orderItem);
}
