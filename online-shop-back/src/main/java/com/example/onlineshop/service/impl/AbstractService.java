package com.example.onlineshop.service.impl;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.onlineshop.dto.SearchCriteria;
import com.example.onlineshop.repository.GenericSpecification;

public class AbstractService<T> {

	protected Specification<T> toSpecifications(final List<SearchCriteria> criterias) {
		return toSpecifications(criterias, null);
	}

	/**
	 * Converts the criteria to specifications
	 *
	 * @param criterias list of criteria
	 * @return an instance of specification
	 */
	protected Specification<T> toSpecifications(final List<SearchCriteria> criterias,
			final Specification<T> initialSpecifications) {
		Specification<T> specifications = initialSpecifications;

		for (final SearchCriteria criteria : criterias) {
			final GenericSpecification<T> userSpecification = new GenericSpecification<>(criteria);

			if (specifications == null) {
				specifications = Specification.where(userSpecification);
			} else {

				specifications = specifications.and(userSpecification);
			}
		}

		return specifications;
	}
}
