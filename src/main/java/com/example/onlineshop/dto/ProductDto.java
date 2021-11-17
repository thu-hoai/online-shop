package com.example.onlineshop.dto;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

	private Long productId;
	
	private String productName;
	
	private String productDescription;
	
	private String categoryCode;
	
	private BigDecimal price;
	
	private Integer productStock;
}
