package com.example.onlineshop.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item")
@Entity
public class OrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private OrderItemId ids;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@MapsId("productId")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	@MapsId("orderId")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_item_status_code")
	private OrderItemStatus orderItemStatus;

	@Column(name = "quantity")
	private Integer quantity;

	@Override
	public String toString() {
		return "OrderItem{" +
				", product=" + product +
				", order=" + order +
				", orderItemStatus=" + orderItemStatus +
				", quantity=" + quantity +
				'}';
	}
}
