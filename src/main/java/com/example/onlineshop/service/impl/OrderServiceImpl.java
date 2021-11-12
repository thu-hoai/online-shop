package com.example.onlineshop.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.entity.Order;
import com.example.onlineshop.entity.OrderItem;
import com.example.onlineshop.entity.OrderItemId;
import com.example.onlineshop.entity.OrderItemStatus;
import com.example.onlineshop.entity.OrderStatus;
import com.example.onlineshop.entity.Product;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.enums.OrderItemStatusCode;
import com.example.onlineshop.enums.OrderStatusCode;
import com.example.onlineshop.mapper.IOrderMapper;
import com.example.onlineshop.repository.OrderItemRepository;
import com.example.onlineshop.repository.OrderRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.service.OrderService;
import com.example.onlineshop.service.exception.InsufficientItemOrderException;
import com.example.onlineshop.service.exception.OrderNotFoundException;
import com.example.onlineshop.service.exception.ProductNotFoundException;
import com.example.onlineshop.service.exception.UserNotFoundException;
import com.example.onlineshop.utils.AuthUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl extends AbstractService<Order> implements OrderService {

	private final OrderRepository orderRepository;

	private final UserRepository userRepository;

	private final IOrderMapper orderMapper;

	private final OrderItemRepository orderItemRepository;

	private final ProductRepository productRepository;


	@Override
	public PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria) {
		Specification<Order> conditions = toSpecifications(criteria);
		final Page<Order> paginatedOrders = orderRepository.findAll(conditions, pageRequest);
		List<OrderDto> orders = orderMapper.convertToOrderDtoList(paginatedOrders.getContent());
		return new PageDto<>(orders, paginatedOrders);
	}
	

	@Override
	public OrderDto placeNewEmptyOrder(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(String.format("User not found %d", userId)));
		Order order = Order.builder()
				.orderStatus(OrderStatus.builder().orderStatusCode(OrderStatusCode.NEW.toString()).build()).user(user)
				.orderDate(LocalDateTime.now()).orderAmount(BigDecimal.ZERO).build();
		return orderMapper.convertToOrderDto(orderRepository.save(order));
	}

	@Transactional
	@Override
	public void addItemToOrder(ItemFormDto itemForm, Long orderId) {
		Order orderEntity = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %d not found", orderId)));

		Product productEntity = productRepository.findById(itemForm.getProductId())
				.orElseThrow(() -> new ProductNotFoundException(
						String.format("Product with id %d not found", itemForm.getProductId())));
		if (productEntity.getProductStock() < itemForm.getQuantity()) {
			throw new InsufficientItemOrderException(String.format("Insufficient item %d", itemForm.getProductId()));
		}
		productEntity.setProductStock(productEntity.getProductStock() - itemForm.getQuantity());

		List<OrderItem> items;
		if (orderEntity.getOrderItem() == null || orderEntity.getOrderItem().isEmpty()) {
			items = addFirstItemToOrder(productEntity, orderEntity, itemForm);
		} else {
			items = mergeItemToCurrentOrder(productEntity, orderEntity, itemForm);
		}

		// Update order amount
		orderEntity.setOrderAmount(calculateAmount(items));
		orderRepository.save(orderEntity);

	}

	@Override
	public OrderDto getOrderById(Long orderId) {
		Order orderEntity = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %d not found", orderId)));
		return orderMapper.convertToOrderDto(orderEntity);
	}

	private List<OrderItem> addFirstItemToOrder(Product productEntity, Order orderEntity, ItemFormDto itemForm) {
		// Save current item
		OrderItem item = OrderItem.builder()
				.ids(new OrderItemId(orderEntity.getOrderId(), productEntity.getProductId()))
				.orderItemStatus(
						OrderItemStatus.builder().orderItemStatusCode(OrderItemStatusCode.AVL.toString()).build())
				.order(orderEntity)
				.product(productEntity)
				.quantity(itemForm.getQuantity()).build();

		OrderItem createdItem = orderItemRepository.save(item);

		return Arrays.asList(createdItem);
	}

	private List<OrderItem> mergeItemToCurrentOrder(Product productEntity, Order orderEntity, ItemFormDto itemForm) {
		List<OrderItem> itemsList = orderItemRepository.findByIdsOrderIdAndIdsProductId(orderEntity.getOrderId(),
				itemForm.getProductId());
		List<OrderItem> currentOrderItems = orderEntity.getOrderItem();
		List<OrderItem> updateOrderItems = new ArrayList<>();

		OrderItem item;
		if (itemsList == null || itemsList.isEmpty()) {
			item = OrderItem.builder().ids(new OrderItemId(orderEntity.getOrderId(), productEntity.getProductId()))
					.orderItemStatus(
							OrderItemStatus.builder().orderItemStatusCode(OrderItemStatusCode.AVL.toString()).build())
					.order(orderEntity).product(productEntity).quantity(itemForm.getQuantity()).build();
		} else {
			item = itemsList.get(0);
			item.setQuantity(item.getQuantity() + itemForm.getQuantity());
		}
		orderItemRepository.save(item);
		updateOrderItems.add(item);

		for (OrderItem orderItem : currentOrderItems) {
			if (!orderItem.getIds().getProductId().equals(item.getIds().getProductId())) {
				updateOrderItems.add(item);
			}
		}
		return updateOrderItems;
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
		return changeOrderStatus(orderDto.getUserId(), orderDto.getOrderId(), OrderStatusCode.CML.toString());
	}

	private BigDecimal calculateAmount(List<OrderItem> orderItem) {
		return orderItem.stream().map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public OrderDto getOrderById(Long userId, Long orderId) {
		Order entityOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %d not found", orderId)));

		return orderMapper.convertToOrderDto(entityOrder);
	}

}
