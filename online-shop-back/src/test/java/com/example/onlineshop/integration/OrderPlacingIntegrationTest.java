package com.example.onlineshop.integration;

import com.example.onlineshop.TestConstants;
import com.example.onlineshop.dto.*;
import com.example.onlineshop.entity.Role;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.enums.OrderStatusCode;
import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.exception.UserNotFoundException;
import com.example.onlineshop.service.facade.OrderPlaceFacadeImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Set;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class OrderPlacingIntegrationTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  @Autowired
  private OrderPlaceFacadeImpl service;

  private final String TEST_USER_DATA_BEFORE_SQL = "/test_user_data.sql";
  private final String TEST_ROLE_DATA_BEFORE_SQL = "/test_role_data.sql";
  private final String TEST_USER_ROLE_DATA_BEFORE_SQL = "/test_user_role_data.sql";
  private final String TEST_PRODUCT_DATA_BEFORE_SQL = "/test_product_data.sql";

  String token;
  JwtUser jwtUser;

  @BeforeEach
  void setUp() throws Exception {

    // Mock security
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
        .apply(SecurityMockMvcConfigurers.springSecurity()).addFilter(springSecurityFilterChain).build();

    Role role = Role.builder().id(TestConstants.ROLE_ID).name(TestConstants.ADMINISTRATOR).build();
    User user = User.builder().id(TestConstants.USER_ID).username(TestConstants.USERNAME).enabled(true)
        .password(TestConstants.PASSWORD).authorities(Set.of(role)).build();

    jwtUser = JwtUser.build(user);

  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_ROLE_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_PRODUCT_DATA_BEFORE_SQL)
  void createUser_ThenPlacingOrder() {
    // Create user
    Role role = Role.builder().id(TestConstants.ROLE_ID).name(TestConstants.ADMINISTRATOR).build();
    UserDto userDto = UserDto.builder().id(TestConstants.USER_ID).username(TestConstants.USERNAME)
      .password(TestConstants.PASSWORD)
      .authorities(Set.of(role)).build();
    JwtUser jwt = service.createUser(userDto);

    // Mock authentication
    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(jwt);
    SecurityContextHolder.setContext(securityContext);

    // place a new order
    ItemFormDto form = new ItemFormDto(TestConstants.PRODUCT_ID_2, TestConstants.PRODUCT_2_QUANTITY);
    OrderDto orderDto = service.placeNewEmptyOrder();
     service.addItemToOrder(form, orderDto.getOrderId());
    OrderDto actual = service.getOrderbyId(orderDto.getOrderId());

    // Get product by Id
    ProductDto productDto = service.getProductById(TestConstants.PRODUCT_ID_2);

    // Then
    Assertions.assertEquals(TestConstants.PRODUCT_2_STOCK - TestConstants.PRODUCT_2_QUANTITY, productDto.getProductStock());
    Assertions.assertEquals(jwt.getId(), actual.getUserId());
    Assertions.assertEquals(OrderStatusCode.NEW.toString(), actual.getOrderStatusCode());

    // Delete user and check if created user is existed
    JwtUser foundUser = service.findUserById(jwt.getId());
    Assertions.assertNotNull(foundUser);
    service.deleteUserById(jwt.getId());
    Assertions.assertThrows(UserNotFoundException.class, () -> service.findUserById(jwt.getId()));
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_PRODUCT_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_USER_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_ROLE_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_USER_ROLE_DATA_BEFORE_SQL)
  void givenValidPermission_WhenAddProduct_ThenSuccess() {

    // Mock authentication
    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn(jwtUser);
    SecurityContextHolder.setContext(securityContext);

    // Add product
    ProductDto request = ProductDto.builder().productName("PD1").productStock(100).categoryCode("BK").build();
    ProductDto productDto = service.addProduct(request);

    // Get product by Id
    ProductDto actualDto = service.getProductById(productDto.getProductId());
    Assertions.assertEquals(productDto.getProductId(), actualDto.getProductId());

    // Get paginated products
    Pageable pageable = PageRequest.of(0, 10);
    String searchToken = "productName:Fountainhead";
    PageDto<ProductDto> paginatedProducts = service.getPaginatedProductByCriteria(searchToken, pageable);
    Assertions.assertEquals(1, paginatedProducts.getContent().size());
  }


}
