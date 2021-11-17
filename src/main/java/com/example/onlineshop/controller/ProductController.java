package com.example.onlineshop.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.onlineshop.constants.WebContants;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.ProductDto;
import com.example.onlineshop.service.facade.OrderPlaceFacade;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(WebContants.PRODUCTS)
@AllArgsConstructor
public class ProductController {

	private final OrderPlaceFacade service;

	@GetMapping
	public PageDto<ProductDto> getPaginatedProductsByCriteria(final Pageable pageRequest,
			@RequestParam(value = "search", required = false) final String search) {
		return service.getPaginatedProductByCriteria(search, pageRequest);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('Administrator')")
	public ProductDto addProduct(@RequestBody ProductDto productDto) {
		return service.addProduct(productDto);
	}

	@GetMapping(value = "/{productId}")
	public ProductDto getProductById(@PathVariable Long productId) {
		return service.getProductById(productId);
	}
}
