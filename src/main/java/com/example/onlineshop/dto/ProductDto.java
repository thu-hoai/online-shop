package com.example.onlineshop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

	private Long productId;
	
	private String productName;
	
	private String productDescription;
	
	private String categoryCode;
	
	private BigDecimal price;
}
