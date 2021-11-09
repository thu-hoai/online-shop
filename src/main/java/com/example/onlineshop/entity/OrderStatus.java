package com.example.onlineshop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Table(name = "ref_order_status")
@Entity
public class OrderStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "order_status_code")
	private String orderStatusCode;

	@Column(name = "order_status_description")
	private String orderStatusDescription;

	@Override
	public String toString() {
		return "OrderStatus [orderStatusCode=" + orderStatusCode + ", orderStatusDescription=" + orderStatusDescription
				+ "]";
	}

}
