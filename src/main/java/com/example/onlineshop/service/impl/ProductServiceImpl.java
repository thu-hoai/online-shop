package com.example.onlineshop.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.ProductDto;
import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.entity.Product;
import com.example.onlineshop.mapper.IProductMapper;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.service.ProductService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {

	private final ProductRepository productRepository;
	
	private final IProductMapper mapper;
	
	@Override
	public PageDto<ProductDto> getPaginatedProductByCriteria(Pageable pageRequest, List<SearchCriteria> criteria) {
		Specification<Product> conditions = toSpecifications(criteria);
		final Page<Product> paginatedProduct = productRepository.findAll(conditions, pageRequest);
		List<ProductDto> products = mapper.convertToProductDtoList(paginatedProduct.getContent());
		return new PageDto<>(products, paginatedProduct);
	}

	@Override
	public ProductDto addProduct(ProductDto productDto) {
		return null;
	}
	
	@Override
	public ProductDto getProductById(Long id) {
		return null;
	}

}
