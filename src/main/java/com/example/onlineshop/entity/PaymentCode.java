package com.example.onlineshop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "ref_payment_code")
@Entity
public class PaymentCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "payment_code", length = 10)
	private String paymentMethodCode;

	@Column(name = "payment_description", length = 50)
	private String paymentDescription;
}
