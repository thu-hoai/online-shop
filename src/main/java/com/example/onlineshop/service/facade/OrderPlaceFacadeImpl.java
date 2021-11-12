package com.example.onlineshop.service.facade;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.ItemFormDto;
import com.example.onlineshop.dto.OrderDto;
import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.ProductDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.OrderItemService;
import com.example.onlineshop.service.OrderService;
import com.example.onlineshop.service.ProductService;
import com.example.onlineshop.service.UserService;
import com.example.onlineshop.utils.AuthUtils;
import com.example.onlineshop.utils.SearchCriteriaUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderPlaceFacadeImpl implements OrderPlaceFacade {

	private final OrderService orderService;

	private final OrderItemService orderItemService;

	private final ProductService productService;

	private final UserService userService;

	@Override
	public OrderDto placeNewEmptyOrder() {
		Long userId = AuthUtils.getCurrentUserLoggedIn().getId();
		return orderService.placeNewEmptyOrder(userId);
	}

	@Override
	public OrderDto addItemToOrder(ItemFormDto itemForm, Long orderId) {
		Long userId = AuthUtils.getCurrentUserLoggedIn().getId();
		orderService.addItemToOrder(itemForm, orderId);
		return orderService.getOrderById(userId, orderId);
	}

	public OrderDto getOrderByOrderId(Long orderId) {
		Long userId = AuthUtils.getCurrentUserLoggedIn().getId();
		return orderService.getOrderById(userId, orderId);
	}

	@Override
	public OrderItemDto modifyItemQuantityOfOrder(Long userId, Long orderItemId, Integer newQuantity) {
		return orderItemService.modifyItemQuantityOfOrder(userId, orderItemId, newQuantity);
	}

	@Override
	public void removeOrderItem(Long userId, Long orderItemId) {
		orderItemService.removeOrderItem(userId, orderItemId);

	}

	@Override
	public OrderDto checkoutTheOrder(OrderDto orderDto) {
		return orderService.checkoutOrder(orderDto);
	}

	@Override
	public OrderDto getOrderbyId(Long userId, Long orderId) {
		return orderService.getOrderById(userId, orderId);
	}

	@Override
	public ProductDto getProductById(Long productId) {
		return productService.getProductById(productId);
	}

	@Override
	public PageDto<OrderDto> findAllOrders(Pageable pageRequest, List<SearchCriteria> criteria) {
		return orderService.findAllOrders(pageRequest, criteria);
	}

	@Override
	public PageDto<ProductDto> getPaginatedProductByCriteria(Long userId, String searchToken,
			final Pageable pageRequest) {
		List<SearchCriteria> criteria = SearchCriteriaUtils.build(searchToken);
		return productService.getPaginatedProductByCriteria(pageRequest, criteria);
	}

	@Override
	public ProductDto addProduct(ProductDto productDto) {
		return productService.addProduct(productDto);
	}

	@Override
	public List<JwtUser> findAllUsers() {
		return userService.findAllUsers();
	}

	@Override
	public JwtUser findUserById(Long id) {
		return userService.findUserById(id);
	}

	@Override
	public JwtUser createUser(UserDto user) {
		return userService.createUser(user);
	}

	@Override
	public void deleteUserById(Long id) {
		userService.deleteUserById(id);
	}

	@Override
	public JwtUser updateUser(UserDto user) {
		return userService.updateUser(user);
	}

}
