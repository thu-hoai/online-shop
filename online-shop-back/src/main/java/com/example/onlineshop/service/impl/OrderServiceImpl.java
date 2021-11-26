package com.example.onlineshop.service.impl;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.entity.*;
import com.example.onlineshop.enums.ExceptionCode;
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
import com.example.onlineshop.utils.ErrorMessageFormatUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
   *
   * @param userId id of user who is in charge of order placing
   * @return orderDto new order already created
   */
  @Override
  public OrderDto placeNewEmptyOrder(Long userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new UserNotFoundException(ErrorMessageFormatUtils.getErrorMessage(ExceptionCode.USER_NOT_FOUND, userId.toString())));
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
      items = addFirstItemToOrder(productEntity, orderEntity, itemForm);
    } else {
      items = mergeItemToCurrentOrder(productEntity, orderEntity, itemForm);
    }
    // Update order amount
    updateAmountOrder(orderEntity);
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

  private void updateAmountOrder(Order orderEntity) {
    orderEntity.setOrderAmount(calculateAmount(orderEntity.getOrderItem()));
    orderRepository.save(orderEntity);
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
  public OrderDto checkoutOrder(OrderDto orderDto) {
    // change status
    return changeOrderStatus(orderDto.getUserId(), orderDto.getOrderId(), OrderStatusCode.CML.toString());
  }

  private List<OrderItem> addFirstItemToOrder(Product productEntity, Order orderEntity, ItemFormDto itemForm) {
    // Save current item
    OrderItem item = OrderItem.builder()
        .ids(new OrderItemId(orderEntity.getOrderId(), productEntity.getProductId()))
        .orderItemStatus(
            OrderItemStatus.builder().orderItemStatusCode(OrderItemStatusCode.AVL.toString()).build())
        .order(orderEntity).product(productEntity).quantity(itemForm.getQuantity()).build();

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

  private BigDecimal calculateAmount(List<OrderItem> orderItem) {
    if (orderItem == null) return BigDecimal.ZERO;
    return orderItem.stream().map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
