package com.example.onlineshop.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderItemId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId;
	
	private Long productId;

	@Override
	public String toString() {
		return "OrderItemId [orderId=" + orderId + ", productId=" + productId + "]";
	}
	
	
}
