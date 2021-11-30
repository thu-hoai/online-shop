package com.example.onlineshop.service.impl;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.entity.Order;
import com.example.onlineshop.entity.OrderItem;
import com.example.onlineshop.entity.OrderItemId;
import com.example.onlineshop.entity.OrderItemStatus;
import com.example.onlineshop.entity.OrderStatus;
import com.example.onlineshop.entity.Product;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.enums.ExceptionCode;
import com.example.onlineshop.enums.OrderItemStatusCode;
import com.example.onlineshop.enums.OrderStatusCode;
import com.example.onlineshop.mapper.IOrderMapper;
import com.example.onlineshop.repository.OrderItemRepository;
import com.example.onlineshop.repository.OrderRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.OrderService;
import com.example.onlineshop.service.exception.InsufficientItemOrderException;
import com.example.onlineshop.service.exception.OrderNotFoundException;
import com.example.onlineshop.service.exception.ProductNotFoundException;
import com.example.onlineshop.service.exception.UserNotFoundException;
import com.example.onlineshop.utils.ErrorMessageFormatUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl extends AbstractService<Order> implements OrderService {

  private final OrderRepository orderRepository;

  private final UserRepository userRepository;

  private final IOrderMapper orderMapper;

  private final OrderItemRepository orderItemRepository;

  private final ProductRepository productRepository;

  /**
   * Find all paginated Orders by criteria
   *
   * @param pageRequest object represents to page
   * @param criteria    represents to criteria to filter
   * @return a page of orders
   */
  @Override
  public PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria) {
    Specification<Order> conditions = toSpecifications(criteria);
    final Page<Order> paginatedOrders = orderRepository.findAll(conditions, pageRequest);
    List<OrderDto> orders = orderMapper.convertToOrderDtoList(paginatedOrders.getContent());
    return new PageDto<>(orders, paginatedOrders);
  }

  /**
   * Place a new empty order
   * Return existing order in case user is in progress order
   *
   * @param userId id of user who is in charge of order placing
   * @return orderDto new order already created
   */
  @Override
  public OrderDto placeNewEmptyOrder(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
      () -> new UserNotFoundException(ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.USER_NOT_FOUND, userId.toString())));
    List<Order> currentOrders = orderRepository.findByUserIdAndOrderStatus(user.getId(), OrderStatus.builder().orderStatusCode(OrderStatusCode.NEW.toString()).build());
    if (!currentOrders.isEmpty()) {
      return orderMapper.convertToOrderDto(currentOrders.get(0));
    }

    Order order = Order.builder()
      .orderStatus(OrderStatus.builder().orderStatusCode(OrderStatusCode.NEW.toString()).build()).user(user)
      .orderDate(LocalDateTime.now()).orderAmount(BigDecimal.ZERO).build();
    return orderMapper.convertToOrderDto(orderRepository.save(order));
  }

  /**
   * Add item to specific order <br>
   * In case adding successful, update the product stock, update the order basket and order amount
   * Throw {@link OrderNotFoundException} in case order is not present <br>
   * Throw {@link ProductNotFoundException} in case the provided product to added is no present <br>
   * Throw {@link InsufficientItemOrderException} in case the provided product is out of stock <br>
   *
   * @param itemForm item to add
   * @param orderId  specific order
   */
  @Transactional
  @Override
  public void addItemToOrder(ItemFormDto itemForm, Long orderId) {
    Order orderEntity = orderRepository.findById(orderId).orElseThrow(
      () -> new OrderNotFoundException(
        ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.ORDER_NOT_FOUND, orderId.toString())));

    Product productEntity = productRepository.findById(itemForm.getProductId())
      .orElseThrow(() -> new ProductNotFoundException(
        ErrorMessageFormatUtils.getErrorMessage(
          ExceptionCode.PRODUCT_NOT_FOUND, itemForm.getProductId().toString())));

    if (productEntity.getProductStock() < itemForm.getQuantity()) {
      throw new InsufficientItemOrderException(
        ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.OUT_OF_STOCK_ORDER_ITEM, itemForm.getProductId().toString()));
    }

    // Update the product stock
    productEntity.setProductStock(productEntity.getProductStock() - itemForm.getQuantity());

    // Update order basket
    List<OrderItem> items;
    if (orderEntity.getOrderItem() == null || orderEntity.getOrderItem().isEmpty()) {
      addFirstItemToOrder(productEntity, orderEntity, itemForm);
    } else {
      mergeItemToCurrentOrder(productEntity, orderEntity, itemForm);
    }

  }

  @Override
  public OrderDto findCurrentCart(Long userId) {
    List<Order> orders = orderRepository.findByUserIdAndOrderStatus(
      userId, OrderStatus.builder().orderStatusCode(
        OrderStatusCode.NEW.toString()).build());
    if (!orders.isEmpty()) {
      return orderMapper.convertToOrderDto(orders.get(0));
    }
    return null;
  }

  @Override
  public void deleteOrderItem(Long orderId, Long productId) {
    List<OrderItem> orderItems = orderItemRepository.findByIdsOrderIdAndIdsProductId(orderId, productId);
    if (!orderItems.isEmpty()) {
      orderItemRepository.deleteById(orderItems.get(0).getIds());
    }
  }

  /**
   * Get order by order id
   * Throw {@link OrderNotFoundException} the order is not found
   *
   * @param orderId
   * @return a searched OrderDto object
   */
  @Override
  public OrderDto getOrderById(Long orderId) {

    Order orderEntity = orderRepository.findById(orderId).orElseThrow(
      () -> new OrderNotFoundException(ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.ORDER_NOT_FOUND, orderId.toString())));

    return orderMapper.convertToOrderDto(orderEntity);
  }

  @Override
  public OrderDto updateAmountOrder(OrderDto order) {
    Order orderEntity = orderRepository.findById(order.getOrderId()).orElseThrow(
      () -> new OrderNotFoundException(ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.ORDER_NOT_FOUND, order.getOrderId().toString())));

    orderEntity.setOrderAmount(calculateAmount(order.getOrderItem()));

    Order updatedOrder = orderRepository.save(orderEntity);
    return orderMapper.convertToOrderDto(updatedOrder);
  }

  /**
   * Change order status
   * Throw {@link OrderNotFoundException} the order is not found
   *
   * @param userId    represents to user who changes status
   * @param orderId   represents to order
   * @param newStatus represents to status to changed
   * @return an updated OrderDto object
   */
  private OrderDto changeOrderStatus(Long userId, Long orderId, String newStatus) {
    Order entityOrder = orderRepository.findById(orderId).orElseThrow(
      () -> new OrderNotFoundException(ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.ORDER_NOT_FOUND, orderId.toString())));
    entityOrder.setOrderStatus(OrderStatus.builder().orderStatusCode(newStatus).build());
    Order modifiedOrder = orderRepository.save(entityOrder);
    return orderMapper.convertToOrderDto(modifiedOrder);
  }

  /**
   * Check out the specific order
   * Throw {@link OrderNotFoundException} the order is not found
   *
   * @param orderDto
   * @return a updated OrderDto object
   */
  @Override
  public OrderDto checkoutOrder(Long orderId, Long userId) {
    // change status
    return changeOrderStatus(
      userId, orderId, OrderStatusCode.CML.toString());
  }

  private void addFirstItemToOrder(Product productEntity, Order orderEntity, ItemFormDto itemForm) {
    // Save current item
    OrderItem item = OrderItem.builder()
      .ids(new OrderItemId(orderEntity.getOrderId(), productEntity.getProductId()))
      .orderItemStatus(
        OrderItemStatus.builder().orderItemStatusCode(OrderItemStatusCode.AVL.toString()).build())
      .order(orderEntity).product(productEntity).quantity(itemForm.getQuantity()).build();

    orderItemRepository.save(item);

  }


  private void mergeItemToCurrentOrder(Product productEntity, Order orderEntity, ItemFormDto itemForm) {
    List<OrderItem> itemsList = orderItemRepository.findByIdsOrderIdAndIdsProductId(orderEntity.getOrderId(),
      itemForm.getProductId());
    List<OrderItem> currentOrderItems = orderEntity.getOrderItem();

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

  }

  private BigDecimal calculateAmount(List<OrderItemDto> orderItem) {
    if (orderItem == null) return BigDecimal.ZERO;
    return orderItem.stream().map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
