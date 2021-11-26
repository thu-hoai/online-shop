package com.example.onlineshop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "user_payment")
@Entity
public class UserPayment {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "user_payment_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_payment_code_payment_code")
	private PaymentCode paymentCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_user_id")
	private User user;

	@Override
	public String toString() {
		return "UserPayment [id=" + id + ", paymentCode=" + paymentCode + ", user=" + user + "]";
	}

}
