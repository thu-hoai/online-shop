package com.example.onlineshop.service;

import com.example.onlineshop.enums.OrderStatusCode;
import com.example.onlineshop.service.exception.InsufficientItemOrderException;
import com.example.onlineshop.service.exception.OrderNotFoundException;
import com.example.onlineshop.service.exception.ProductNotFoundException;
import com.example.onlineshop.service.exception.UserNotFoundException;
import com.example.onlineshop.utils.Pojo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = Pojo.class)
class TestPojo {
//
//  @Autowired
//  @InjectMocks
//  private Pojo pojo;
  @Test
  void givenBuilderWithDefaultValue_NoArgsWorksAlso() {
    Pojo build = new Pojo().toBuilder().build();
    Pojo pojo = new Pojo();
    Assertions.assertEquals(build.getName(), pojo.getName());
    Assertions.assertTrue(build.isOriginal() == pojo.isOriginal());
  }
}
