package com.example.onlineshop.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto<T> {
	/** The content of the page */
	private List<T> content;

	/** Total pages */
	private long totalPages;

	/** Number of elements */
	private long totalElements;

	/** Is it the first page */
	private boolean first;

	/** Is it the last page */
	private boolean last;

	/**
	 * Constructor
	 * 
	 * @param content the list of elements
	 * @param page    contains infos on the page
	 */
	public PageDto(final List<T> content, final Page<?> page) {
		this.content = content;
		totalPages = page.getTotalPages();
		totalElements = page.getTotalElements();
		first = page.isFirst();
		last = page.isLast();
	}
}
