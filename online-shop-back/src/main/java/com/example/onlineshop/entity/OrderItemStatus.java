package com.example.onlineshop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "ref_order_item_status")
@Entity
public class OrderItemStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "order_item_status_code")
	private String orderItemStatusCode;

	@Column(name = "item_status_description")
	private String itemStatusDescription;

	@Override
	public String toString() {
		return "OrderItemStatus [orderItemStatusCode=" + orderItemStatusCode + ", itemStatusDescription="
				+ itemStatusDescription + "]";
	}

}
