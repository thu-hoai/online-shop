package com.example.onlineshop.controller;

import com.example.onlineshop.TestConstants;
import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.entity.Role;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.repository.RoleRepository;
import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.facade.OrderPlaceFacadeImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class ControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  @Autowired
  private OrderPlaceFacadeImpl service;

  @Autowired
  private RoleRepository roleRepository;

  private final String TEST_USER_DATA_BEFORE_SQL = "/test_user_data.sql";
  private final String TEST_ROLE_DATA_BEFORE_SQL = "/test_role_data.sql";
  private final String TEST_USER_ROLE_DATA_BEFORE_SQL = "/test_user_role_data.sql";

  String token;
  JwtUser jwtUser;

  @BeforeEach
  void setUp() throws Exception {

    // Mock Spring security
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
        .apply(SecurityMockMvcConfigurers.springSecurity()).addFilter(springSecurityFilterChain).build();
    Role role = Role.builder().id(TestConstants.ROLE_ID).name(TestConstants.ADMINISTRATOR).build();
    User user = User.builder().id(TestConstants.USER_ID).username(TestConstants.USERNAME).enabled(true)
        .password(TestConstants.PASSWORD).authorities(Set.of(role)).build();
    jwtUser = JwtUser.build(user);
  }


  @Test
  void should_ProvidesController_WhenGivenWac() throws Exception {
    final ServletContext servletContext = webApplicationContext.getServletContext();
    Assertions.assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    Assertions.assertNotNull(webApplicationContext.getBean("userController"));
    Assertions.assertNotNull(webApplicationContext.getBean("authController"));
    Assertions.assertNotNull(webApplicationContext.getBean("productController"));
    Assertions.assertNotNull(webApplicationContext.getBean("orderController"));

  }

  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_USER_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_ROLE_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_USER_ROLE_DATA_BEFORE_SQL)
  @Test
  void givenValidRole_WhenGetOrderListByCriteria_ThenReturnOk() throws Exception {
    token = obtainAccessToken(TestConstants.USERNAME, TestConstants.PASSWORD);
    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/orders").header("Authorization", "Bearer " + token))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(jsonPath("$.content", hasSize(0)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.totalPages", CoreMatchers.is(0)))
        .andExpect(jsonPath("$.totalElements", CoreMatchers.is(0)));
  }


  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_USER_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_ROLE_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_USER_ROLE_DATA_BEFORE_SQL)
  @Test
  void givenValidRole_WhenPlaceNewEmptyOrder_ThenReturnOk() throws Exception {
    token = obtainAccessToken(TestConstants.USERNAME, TestConstants.PASSWORD);
    String jsonBody = "{\n" +
        "    \"productId\": 1,\n" +
        "    \"quantity\": 2\n" +
        "}";
    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/orders").header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_USER_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_ROLE_DATA_BEFORE_SQL)
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = TEST_USER_ROLE_DATA_BEFORE_SQL)
  void should_PerformSuccessfulGetUsers_WhenValidToken() throws Exception {
    token = obtainAccessToken(TestConstants.USERNAME, TestConstants.PASSWORD);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", "Bearer " + token))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
  }
  
  @Test
  void should_Return401_WhenInvalidToken() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get("/users/1")).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }

  private String obtainAccessToken(String username, String password) throws Exception {
    String jsonBody = "{\r\n" + "    \"username\": \"" + username + "\",\r\n" + "    \"password\": \"" + password
        + "\"\r\n" + "}";
    ResultActions result = this.mockMvc
        .perform(MockMvcRequestBuilders.post("/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    String resultString = result.andReturn().getResponse().getContentAsString();
    JacksonJsonParser jsonParser = new JacksonJsonParser();
    return jsonParser.parseMap(resultString).get("token").toString();
  }

}
