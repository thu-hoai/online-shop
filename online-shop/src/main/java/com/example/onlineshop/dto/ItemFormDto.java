package com.example.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemFormDto {

	@NotEmpty
	private Long productId;

	@Min(value = 1)
	private Integer quantity;
}
