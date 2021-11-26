package com.example.onlineshop.dto;

import com.example.onlineshop.enums.SearchOperation;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SearchCriteria {
	/** Search field */
	@NonNull
	private String key;

	/** Search operation */
	@NonNull
	private SearchOperation operation;

	private int page;

	private int size;

	private String sortField;

	private int sortOrder;

	/** Search value */
	@NonNull
	private Object value;

	public String getStringValue() {
		return (String) value;
	}
}
