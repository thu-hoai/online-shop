package com.example.onlineshop.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.onlineshop.dto.ProductDto;
import com.example.onlineshop.entity.Product;

@Mapper
public interface IProductMapper {

	@Mapping(source = "categoryCode.categoryCode", target = "categoryCode")
	ProductDto convertToProductDto(Product product);

	List<ProductDto> convertToProductDtoList(List<Product> product);
}
