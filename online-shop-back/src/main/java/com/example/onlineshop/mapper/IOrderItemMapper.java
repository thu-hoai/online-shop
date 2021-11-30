package com.example.onlineshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.entity.OrderItem;

@Mapper
public interface IOrderItemMapper {

	@Mapping(source = "ids.productId", target = "productId")
	@Mapping(source = "ids.orderId", target = "orderId")
	@Mapping(source = "product.productName", target = "productName")
	@Mapping(source = "product.productStock", target = "productStock")
	@Mapping(source = "orderItemStatus.orderItemStatusCode", target = "status")
	@Mapping(source = "product.price", target = "price")
	public OrderItemDto convertToItemDto(OrderItem orderItem);
}
