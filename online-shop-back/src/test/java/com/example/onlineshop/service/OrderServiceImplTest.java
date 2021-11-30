package com.example.onlineshop.service;

import java.math.BigDecimal;
import java.util.*;

import com.example.onlineshop.entity.*;
import com.example.onlineshop.enums.OrderStatusCode;
import com.example.onlineshop.service.exception.InsufficientItemOrderException;
import com.example.onlineshop.service.exception.OrderNotFoundException;
import com.example.onlineshop.service.exception.ProductNotFoundException;
import com.example.onlineshop.service.exception.UserNotFoundException;
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

import com.example.onlineshop.TestConstants;
import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.PageDto;
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
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    IOrderMapper orderMapper;

    @MockBean
    OrderItemRepository orderItemRepository;

    @MockBean
    ProductRepository productRepository;

    @Autowired
    @InjectMocks
    OrderServiceImpl service;


    @Test
    void givenUserId_whenPlaceNewEmptyOrder_ThenSuccessful() {
        // given
        User mockedUser = User.builder().id(TestConstants.USER_ID)
                .username(TestConstants.USERNAME)
                .build();
        Order mockedOrder = Order.builder()
                .orderId(TestConstants.ORDER_ID)
                .orderStatus(OrderStatus.builder().orderStatusCode(OrderStatusCode.NEW.toString()).build())
                .user(mockedUser)
                .orderAmount(BigDecimal.ZERO).build();
        OrderDto mockedOrderDto = OrderDto.builder()
                .orderId(TestConstants.ORDER_ID)
                .userId(mockedUser.getId())
                .orderStatusCode(OrderStatusCode.NEW.toString())
                .orderAmount(BigDecimal.ZERO)
                .build();

        Mockito.when(userRepository.findById(TestConstants.USER_ID))
                .thenReturn(Optional.of(mockedUser));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(mockedOrder);
        Mockito.when(orderMapper.convertToOrderDto(Mockito.any(Order.class))).thenReturn(mockedOrderDto);

        // when
        OrderDto actual = service.placeNewEmptyOrder(TestConstants.USER_ID);

        // then
        Assertions.assertEquals(TestConstants.ORDER_ID, actual.getOrderId());
        Assertions.assertEquals(TestConstants.USER_ID, actual.getUserId());
        Assertions.assertEquals(BigDecimal.ZERO, actual.getOrderAmount());
    }

    @Test
    void givenInvalidUserId_WhenPlaceNewEmptyOrder_ThenThrowNotFoundException() {

        // when then
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            service.placeNewEmptyOrder(TestConstants.USER_ID);
        });
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
    }

    @Test
    void givenInvalidOrderId_WhenAddItemToOrder_ThenThrowOrderNotFoundException() {
        Assertions.assertThrows(OrderNotFoundException.class, () -> {
            ArgumentCaptor<ItemFormDto> formArg = ArgumentCaptor.forClass(ItemFormDto.class);
            service.addItemToOrder(formArg.capture(), TestConstants.ORDER_ID);
        });
    }

    @Test
    void givenInvalidProductId_WhenAddItemToOrder_ThenThrowProductNotFound() {
        // given
        ItemFormDto mockedForm = new ItemFormDto(TestConstants.PRODUCT_ID_1, 2);
        Order mockedOrder = Order.builder()
                .orderId(TestConstants.ORDER_ID).build();
        Mockito.when(orderRepository.findById(TestConstants.ORDER_ID)).thenReturn(Optional.of(mockedOrder));

        // when then
        Assertions.assertThrows(ProductNotFoundException.class, () -> {
            service.addItemToOrder(mockedForm, TestConstants.ORDER_ID);
        });
        Mockito.verify(orderRepository, Mockito.times(1)).findById(Mockito.any());

    }

    @Test
    void givenOutOfStockProduct_WhenAddItemToOrder_ThenThrowInsufficientItemOrderException() {
        // given
        ItemFormDto argForm = new ItemFormDto(TestConstants.PRODUCT_ID_1, 1000);
        Order mockedOrder = Order.builder()
                .orderId(TestConstants.ORDER_ID).build();
        Mockito.when(orderRepository.findById(TestConstants.ORDER_ID)).thenReturn(Optional.of(mockedOrder));
        Product productMock = Product.builder()
                .productId(TestConstants.PRODUCT_ID_1)
                .price(TestConstants.PRODUCT_1_PRICE)
                .productStock(TestConstants.PRODUCT_1_STOCK).build();
        Mockito.when(productRepository.findById(TestConstants.PRODUCT_ID_1))
                .thenReturn(Optional.of(productMock));

        // when then
        Assertions.assertThrows(InsufficientItemOrderException.class, () -> {
            service.addItemToOrder(argForm, TestConstants.ORDER_ID);
        });
        Mockito.verify(orderRepository, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.any());
    }


    @Test
    void givenOrderFormAndOrderIdWithNonEmptyOrder_WhenAddItemToOrder_ThenUpdateOrderOk() {
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
    }

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
    void givenOrderId_WhenGetOrderById_ThenReturnOrderDto() {
        // given
        Order orderMock = Order.builder().orderId(TestConstants.ORDER_ID).orderAmount(null).build();
        OrderDto orderDtoMock = OrderDto.builder().orderId(TestConstants.ORDER_ID).build();
        Mockito.when(orderRepository.findById(Mockito.any())).thenReturn(Optional.of(orderMock));
        Mockito.when(orderMapper.convertToOrderDto(Mockito.any())).thenReturn(orderDtoMock);

        // when
        OrderDto actual = service.getOrderById(TestConstants.ORDER_ID);

        // then
        Assertions.assertEquals(TestConstants.ORDER_ID, actual.getOrderId());
    }

    @Test
    void givenInvalidOderId_whenGetOrderById_ThenThrowOrderNotFoundException() {
        Assertions.assertThrows(OrderNotFoundException.class, () -> {
            service.getOrderById(TestConstants.ORDER_ID);
        });
    }

    @Test
    void givenValidArguments_WhenCheckoutOrder_ThenReturnUpdatedStatus() {
        // given
        OrderDto orderDtoMock = OrderDto.builder().orderId(TestConstants.ORDER_ID)
                .userId(TestConstants.USER_ID).build();
        Order orderMock = Order.builder().orderId(TestConstants.ORDER_ID).orderAmount(null).build();
        Order updatedOrderMock = Order.builder().orderId(TestConstants.ORDER_ID)
                .orderStatus(OrderStatus.builder().orderStatusCode(OrderStatusCode.CML.toString()).build())
                .orderAmount(BigDecimal.TEN).build();
        Mockito.when(orderRepository.findById(TestConstants.ORDER_ID)).thenReturn(Optional.of(orderMock));
        Mockito.when(orderRepository.save(orderMock)).thenReturn(updatedOrderMock);
        orderDtoMock.setOrderStatusCode(OrderStatusCode.CML.toString());
        Mockito.when(orderMapper.convertToOrderDto(updatedOrderMock)).thenReturn(orderDtoMock);

        // when
        OrderDto actual = service.checkoutOrder(orderDtoMock.getOrderId(), orderDtoMock.getUserId());
        // then
        Assertions.assertEquals(OrderStatusCode.CML.toString(), actual.getOrderStatusCode());
    }

    @Test
    void givenInvalidOrderId_WhenCheckoutOrder_ThenThrowOrderNotFoundException() {
        // given
        OrderDto orderDtoMock = OrderDto.builder().orderId(TestConstants.ORDER_ID)
                .userId(TestConstants.USER_ID).build();
        Assertions.assertThrows(OrderNotFoundException.class, () -> {
            service.checkoutOrder(orderDtoMock.getOrderId(), orderDtoMock.getUserId());
        });
        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any(Order.class));
    }
}
