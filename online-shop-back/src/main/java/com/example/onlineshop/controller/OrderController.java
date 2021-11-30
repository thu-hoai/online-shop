package com.example.onlineshop.controller;

import java.util.List;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.onlineshop.constants.WebContants;
import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.service.facade.OrderPlaceFacade;
import com.example.onlineshop.utils.SearchCriteriaUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(WebContants.ORDERS)
@AllArgsConstructor
public class OrderController {

	private final OrderPlaceFacade service;

	@GetMapping
	public PageDto<OrderDto> getOrderListByCriteria(final Pageable pageRequest,
			@RequestParam(value = "search", required = false) final String search) {
		List<SearchCriteria> criteria = SearchCriteriaUtils.build(search);
		return service.findAllOrders(pageRequest, criteria);
	}

	/**
	 * Place a new empty order
	 * @return
	 */
	@PostMapping
	public OrderDto placeNewEmptyOrder() {
		return service.placeNewEmptyOrder();
	}

	/**
	 * Add product to the current shopping cart
	 * 
	 * @param cartDto
	 * @return
	 */
	@PostMapping(value = "/{orderId}")
	public OrderDto addToShoppingCart(@PathVariable Long orderId, @RequestBody ItemFormDto cartDto) {
		return service.addItemToOrder(cartDto, orderId);
	}

	@PutMapping(value = "/{orderId}")
	public OrderDto checkoutOrder(@PathVariable Long orderId) {
		return service.checkoutTheOrder(orderId);
	}

	@GetMapping(value = "/{orderId}")
	public OrderDto getOrderByOrderId(@PathVariable Long orderId) {
		return  service.getOrderbyId(orderId);
	}

	@GetMapping(value = "/current-order")
	public OrderDto getCurrentCart() {
		return service.findCurrentCart();
	}

	@DeleteMapping(value = "/{orderId}")
	public void deleteOrderItem(@PathVariable Long orderId, @RequestParam(name = "productId", required = true) Long productId) {
		 service.deleteOrderItem(orderId, productId);
	}

}
