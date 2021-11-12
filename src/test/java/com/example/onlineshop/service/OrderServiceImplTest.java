package com.example.onlineshop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.onlineshop.constants.TestConstants;
import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.entity.Order;
import com.example.onlineshop.entity.OrderItem;
import com.example.onlineshop.entity.OrderItemId;
import com.example.onlineshop.entity.Product;
import com.example.onlineshop.mapper.IOrderMapper;
import com.example.onlineshop.repository.OrderItemRepository;
import com.example.onlineshop.repository.OrderRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.service.impl.OrderServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = OrderServiceImpl.class)
class OrderServiceImplTest {

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private IOrderMapper orderMapper;

	@MockBean
	private OrderItemRepository orderItemRepository;

	@MockBean
	private ProductRepository productRepository;

	@Autowired
	@InjectMocks
	private OrderServiceImpl service;
	
	@Test
	void givenPageableAndCriteria_WhenFindAllOrders_ThenReturnPageDtoOrders() {
		// given
		Order orderMock = Order.builder().orderId(TestConstants.ORDER_ID).orderAmount(null).build();
		OrderDto orderDtoMock = OrderDto.builder().orderId(TestConstants.ORDER_ID).build();
		Page<Order> paginatedOrders = new PageImpl<>(Arrays.asList(orderMock));
		ArgumentCaptor<Specification> argsCaptor = ArgumentCaptor.forClass(Specification.class);
		Mockito.when(orderRepository.findAll(argsCaptor.capture(), Mockito.any(Pageable.class))).thenReturn(paginatedOrders);
		Mockito.when(orderMapper.convertToOrderDtoList(Mockito.any())).thenReturn(Arrays.asList(orderDtoMock));
		// when
		PageDto<OrderDto> actual = service.findAllOrders(PageRequest.of(0, 20), new ArrayList<>());
		// then
		Assertions.assertEquals(TestConstants.ORDER_ID, actual.getContent().get(0).getOrderId());
	}

	@Test
	void givenOrderFormAndOrderIdWithEmptyOrder_WhenAddItemToOrder_ThenUpdateOrderOk() {
		// given
		ItemFormDto itemFormMock = new ItemFormDto(TestConstants.PRODUCT_ID_2, TestConstants.PRODUCT_2_QUANTITY);
		Long orderId = TestConstants.ORDER_ID;

		Product productMock = Product.builder().productId(TestConstants.PRODUCT_ID_1)
				.price(TestConstants.PRODUCT_1_PRICE).productStock(TestConstants.PRODUCT_1_STOCK).build();

		Order orderMock = Order.builder().orderId(orderId).orderAmount(null).build();

		OrderItem itemMock = OrderItem.builder()
				.ids(new OrderItemId(orderMock.getOrderId(), productMock.getProductId()))
				.order(orderMock)
				.product(productMock)
				.quantity(itemFormMock.getQuantity()).build();
		Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderMock));
		Mockito.when(productRepository.findById(itemFormMock.getProductId())).thenReturn(Optional.of(productMock));
		Mockito.when(orderItemRepository.save(Mockito.any(OrderItem.class))).thenReturn(itemMock);
		Mockito.when(orderRepository.save(orderMock)).thenReturn(orderMock);
		// when
		service.addItemToOrder(itemFormMock, orderId);

		// then
		Mockito.verify(orderItemRepository, Mockito.times(1)).save(Mockito.any(OrderItem.class));
		Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
	}
	
	@Test
	void givenOrderFormAndOrderIdWithNonEmptyOrder__WhenAddItemToOrder_ThenUpdateOrderOk() {
		// given
		ItemFormDto itemFormMock = new ItemFormDto(TestConstants.PRODUCT_ID_2, TestConstants.PRODUCT_2_QUANTITY);
		Long orderId = TestConstants.ORDER_ID;

		Product productMock = Product.builder().productId(TestConstants.PRODUCT_ID_1)
				.price(TestConstants.PRODUCT_1_PRICE).productStock(TestConstants.PRODUCT_1_STOCK).build();
		
		OrderItem itemMock = OrderItem.builder()
				.ids(new OrderItemId(orderId, productMock.getProductId()))
				.product(productMock)
				.quantity(itemFormMock.getQuantity()).build();

		Order orderMock = Order.builder().orderId(orderId).orderItem(Arrays.asList(itemMock)).orderAmount(null).build();
		Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderMock));
		Mockito.when(productRepository.findById(itemFormMock.getProductId())).thenReturn(Optional.of(productMock));
		Mockito.when(orderItemRepository.findByIdsOrderIdAndIdsProductId(Mockito.any(), Mockito.any())).thenReturn(Arrays.asList(itemMock));
		Mockito.when(orderRepository.save(orderMock)).thenReturn(orderMock);
		Mockito.when(orderItemRepository.save(Mockito.any(OrderItem.class))).thenReturn(itemMock);
		
		// when
		service.addItemToOrder(itemFormMock, orderId);
		
		// then
		Mockito.verify(orderItemRepository, Mockito.times(1)).save(Mockito.any(OrderItem.class));
		Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
	}

}
