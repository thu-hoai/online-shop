package com.example.onlineshop.service;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.service.exception.InsufficientItemOrderException;
import com.example.onlineshop.service.exception.OrderNotFoundException;
import com.example.onlineshop.service.exception.ProductNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    /**
     * Find all paginated Orders by criteria
     *
     * @param pageRequest object represents to page
     * @param criteria    represents to criteria to filter
     * @return a page of orders
     */
    PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria);

    /**
     * Place a new empty order
     *
     * @param userId id of user who is in charge of order placing
     * @return orderDto new order already created
     */
    OrderDto placeNewEmptyOrder(Long userId);

    /**
     * Check out the specific order
     * Throw {@link OrderNotFoundException} the order is not found
     *
     * @param orderDto
     * @return a updated OrderDto object
     */
    OrderDto checkoutOrder(OrderDto orderDto);

    /**
     * Get order by order id
     * Throw {@link OrderNotFoundException} the order is not found
     *
     * @param orderId
     * @return a searched OrderDto object
     */
    OrderDto getOrderById(Long orderId);

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
    void addItemToOrder(ItemFormDto itemForm, Long orderId);

}
