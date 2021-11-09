package com.example.onlineshop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineshop.constants.WebContants;
import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.entity.OrderItem;
import com.example.onlineshop.entity.Product;
import com.example.onlineshop.service.OrderItemService;
import com.example.onlineshop.service.OrderPlaceFacade;
import com.example.onlineshop.service.OrderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(WebContants.ORDER_ITEMS)
@AllArgsConstructor
public class OrderItemController {

	private final OrderPlaceFacade orderPlacingService;

	/**
	 * Add an product to the current shopping cart
	 * 
	 * @param cartDto
	 * @return
	 */
	@PostMapping("/add")
	public OrderItemDto addToShoppingCart(ItemFormDto cartDto) {
		return orderPlacingService.addItemToOrder(cartDto, null);
	}

}
