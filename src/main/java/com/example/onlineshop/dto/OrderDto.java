package com.example.onlineshop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

	private Long orderId;

	private Long userId;

	private String orderStatusCode;

	private List<OrderItemDto> orderItem;

	private LocalDateTime orderDate;

	private BigDecimal orderAmount;
}
