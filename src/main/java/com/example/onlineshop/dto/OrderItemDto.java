package com.example.onlineshop.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

	private Long id;

	private Long productId;
	
	private BigDecimal price;

	private Long orderId;

	private String status;

	private Integer quantity;

}
