package com.example.onlineshop.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineshop.constants.WebContants;
import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.OrderItemDto;
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
	
	@PostMapping
	public OrderDto placeNewEmptyOrder() {
		return service.placeNewEmptyOrder();
	}
	
	/**
	 * Add an product to the current shopping cart
	 * 
	 * @param cartDto
	 * @return
	 */
	@PostMapping(value = "{orderId}/add")
	public OrderDto addToShoppingCart(@PathVariable Long orderId,
			@RequestBody ItemFormDto cartDto) {
		return service.addItemToOrder(cartDto, orderId);
	}
}
