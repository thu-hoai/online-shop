package com.example.onlineshop.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.onlineshop.dto.PageDto;
import com.example.onlineshop.dto.ProductDto;
import com.example.onlineshop.dto.SearchCriteria;

public interface ProductService {

	PageDto<ProductDto> getPaginatedProductByCriteria(Pageable pageRequest, List<SearchCriteria> criteria);
	
	ProductDto addProduct(ProductDto productDto);
	
}
