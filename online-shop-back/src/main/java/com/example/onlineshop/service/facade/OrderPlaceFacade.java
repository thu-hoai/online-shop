package com.example.onlineshop.service.facade;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.ProductDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.security.dto.JwtUser;

public interface OrderPlaceFacade {

	/**
	 * Create a new empty order
	 */
	OrderDto placeNewEmptyOrder();

	/**
	 * Add an item to the specific order
	 * 
	 * @param itemForm
	 * @return
	 */
	OrderDto addItemToOrder(ItemFormDto itemForm, Long orderId);


	/**
	 * Get order by order id. Calculate the temp order amount
	 * 
	 * @param orderId
	 */
	OrderDto getOrderbyId(Long orderId);

	PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria);

	OrderDto findCurrentCart();

	/**
	 * Checkout the shopping cart
	 * 
	 * @return
	 */
	OrderDto checkoutTheOrder(Long orderId);

	ProductDto getProductById(Long productId);

	PageDto<ProductDto> getPaginatedProductByCriteria(String searchToken, Pageable pageRequest);

	ProductDto addProduct(ProductDto productDto);

	List<JwtUser> findAllUsers();

	JwtUser findUserById(Long id);

	JwtUser createUser(UserDto user);

	void deleteUserById(Long id);

	JwtUser updateUser(UserDto user);


	void deleteOrderItem(Long orderId, Long productId);
}
