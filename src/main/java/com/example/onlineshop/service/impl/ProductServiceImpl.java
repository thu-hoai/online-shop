package com.example.onlineshop.service.impl;

import java.util.List;

import com.example.onlineshop.entity.ProductCategory;
import com.example.onlineshop.enums.ExceptionCode;
import com.example.onlineshop.service.exception.ProductNotFoundException;
import com.example.onlineshop.utils.ErrorMessageFormatUtils;
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
			Product product = Product.builder().productName(productDto.getProductName())
					.productDescription(productDto.getProductDescription())
					.productStock(productDto.getProductStock())
					.categoryCode(ProductCategory.builder().categoryCode(productDto.getCategoryCode()).build())
					.price(productDto.getPrice()).build();
			return mapper.convertToProductDto(productRepository.save(product));
	}
	
	@Override
	public ProductDto getProductById(Long id) {
		Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
					ErrorMessageFormatUtils.getErrorMessage(
							ExceptionCode.PRODUCT_NOT_FOUND, id.toString())));
		return mapper.convertToProductDto(product);
	}

}
