package com.example.onlineshop.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.entity.Order;
import com.example.onlineshop.entity.OrderStatus;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.enums.OrderStatusCode;
import com.example.onlineshop.mapper.IOrderMapper;
import com.example.onlineshop.repository.OrderRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.service.OrderService;
import com.example.onlineshop.service.exception.OrderNotFoundException;
import com.example.onlineshop.service.exception.UserNotFoundException;
import com.example.onlineshop.utils.AuthUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl extends AbstractService<Order> implements OrderService {

	private final OrderRepository orderRepository;

	private final UserRepository userRepository;

	private final IOrderMapper orderMapper;

	@Override
	public PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria) {
		Specification<Order> conditions = toSpecifications(criteria);
		final Page<Order> paginatedOrders = orderRepository.findAll(conditions, pageRequest);
		List<OrderDto> orders = orderMapper.convertToOrderDtoList(paginatedOrders.getContent());
		return new PageDto<>(orders, paginatedOrders);
	}

	@Override
	public OrderDto findOrderById(Long id) {
		return null;
	}

	@Override
	public OrderDto placeNewEmptyOrder(Long userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(String.format("User not found %d", userId)));
		Order order = Order.builder()
				.orderStatus(OrderStatus.builder().orderStatusCode(OrderStatusCode.NEW.toString()).build()).user(user)
				.orderDate(LocalDateTime.now())
				.orderAmount(BigDecimal.ZERO).build();
		return orderMapper.convertToOrderDto(orderRepository.save(order));
	}

	@Override
	public OrderDto changeOrderStatus(Long userId, Long orderId, String newStatus) {
		Order entityOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %d not found", orderId)));
		entityOrder.setOrderStatus(OrderStatus.builder().orderStatusCode(newStatus).build());
		Order modifiedOrder = orderRepository.save(entityOrder);
		return orderMapper.convertToOrderDto(modifiedOrder);
	}

	@Override
	public OrderDto checkoutOrder(OrderDto orderDto) {
		AuthUtils.getAuthorizedUser(orderDto.getUserId());
		// change status
		OrderDto modifiedDto = changeOrderStatus(orderDto.getUserId(), orderDto.getOrderId(),
				OrderStatusCode.CML.toString());
		// calculate
		modifiedDto.setOrderAmount(calculateOrderAmount(orderDto.getOrderItem()));
		return modifiedDto;
	}

	private BigDecimal calculateOrderAmount(List<OrderItemDto> orderItem) {
		return orderItem.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public OrderDto getOrderById(Long userId, Long orderId) {
		Order entityOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %d not found", orderId)));

		return orderMapper.convertToOrderDto(entityOrder);
	}

}
