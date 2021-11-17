package com.example.onlineshop.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ref_product_category")
@Entity
public class ProductCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "category_code")
	private String categoryCode;

	@Column(name = "category_name")
	private String categoryName;

	@Column(name = "category_description")
	private String categoryDescription;

	@Override
	public String toString() {
		return "ProductCategory [categoryCode=" + categoryCode + ", categoryName=" + categoryName
				+ ", categoryDescription=" + categoryDescription + "]";
	}

}
