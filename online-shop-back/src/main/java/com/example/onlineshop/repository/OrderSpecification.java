package com.example.onlineshop.repository;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import com.example.onlineshop.dto.SearchCriteria;

public class OrderSpecification<OrderItem> implements Specification<OrderItem> {

  private static final long serialVersionUID = 1L;

  @NonNull
  private SearchCriteria criteria;

  @Override
  public Predicate toPredicate(final Root<OrderItem> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

    switch (criteria.getOperation()) {
      case EQUALITY:
        return builder.equal(root.get(criteria.getKey()), criteria.getValue());
      case NEGATION:
        return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
      case GREATER_THAN:
        return builder.greaterThan(root.<String>get(criteria.getKey()), criteria.getStringValue());
      case LESS_THAN:
        return builder.lessThan(root.<String>get(criteria.getKey()), criteria.getStringValue());
      case LIKE:
        return builder.like(root.<String>get(criteria.getKey()), criteria.getStringValue());
      case STARTS_WITH:
        return builder.like(builder.upper(root.<String>get(criteria.getKey())),
          criteria.getStringValue().toUpperCase() + "%");
      case ENDS_WITH:
        return builder.like(builder.upper(root.<String>get(criteria.getKey())),
          "%" + criteria.getStringValue().toUpperCase());
      case CONTAINS:
        return builder.like(builder.upper(root.<String>get(criteria.getKey())),
          "%" + criteria.getStringValue().toUpperCase() + "%");
      default:
        return null;
    }
  }
}