package com.example.onlineshop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Entity
@Table(name = "order")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long orderId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_status_code")
	private OrderStatus orderStatus;

	@Column(name = "date_order_placed")
	private LocalDateTime orderDate;
	
	@OneToMany(mappedBy="order")
	private List<OrderItem> orderItem;
	
	@Column(name = "amount")
	private BigDecimal orderAmount; 

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", user=" + user + ", orderStatus=" + orderStatus + ", orderDate="
				+ orderDate + "]";
	}

}
