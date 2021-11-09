package com.example.onlineshop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.entity.Order;

@Mapper(uses = { IOrderItemMapper.class })
public interface IOrderMapper {

	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "orderStatus.orderStatusCode", target = "orderStatusCode")
	OrderDto convertToOrderDto(Order order);

	List<OrderDto> convertToOrderDtoList(List<Order> orders);

}
