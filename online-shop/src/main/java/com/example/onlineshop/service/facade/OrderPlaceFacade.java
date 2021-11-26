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
	public OrderDto placeNewEmptyOrder();

	/**
	 * Add an item to the specific order
	 * 
	 * @param itemForm
	 * @return
	 */
	public OrderDto addItemToOrder(ItemFormDto itemForm, Long orderId);


	/**
	 * Get order by order id. Calculate the temp order amount
	 * 
	 * @param userId
	 * @param orderId
	 * @return
	 */
	public OrderDto getOrderbyId(Long userId, Long orderId);

	public PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria);

	/**
	 * Checkout the shopping cart
	 * 
	 * @param orderDto
	 * @return
	 */
	public OrderDto checkoutTheOrder(OrderDto orderDto);

	public ProductDto getProductById(Long productId);

	public PageDto<ProductDto> getPaginatedProductByCriteria(String searchToken, Pageable pageRequest);

	public ProductDto addProduct(ProductDto productDto);

	public List<JwtUser> findAllUsers();

	public JwtUser findUserById(Long id);

	public JwtUser createUser(UserDto user);

	public void deleteUserById(Long id);

	public JwtUser updateUser(UserDto user);

}
