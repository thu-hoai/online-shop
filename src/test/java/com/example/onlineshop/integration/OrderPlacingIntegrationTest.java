package com.example.onlineshop.integration;

import com.example.onlineshop.TestConstants;
import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.entity.Role;
import com.example.onlineshop.repository.RoleRepository;
import com.example.onlineshop.service.facade.OrderPlaceFacadeImpl;
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

    @Autowired
    private RoleRepository roleRepository;

    String token;

    @BeforeEach
    void setUp() throws Exception {

        // Mock Spring security
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity()).addFilter(springSecurityFilterChain).build();
        Role role = Role.builder().id(TestConstants.ROLE_ID).name(TestConstants.ADMINISTRATOR).build();

        // Prepare data
        roleRepository.save(role);
        UserDto user = new UserDto();
        user.setId(TestConstants.USER_ID);
        user.setUsername(TestConstants.USERNAME);
        user.setEmail(TestConstants.EMAIL);
        user.setPassword(TestConstants.PASSWORD);
        user.setAuthorities(Set.of(role));
        service.createUser(user);
        token = obtainAccessToken();

    }

    @Test
    void test() throws Exception {
//		this.mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", "Bearer " + token))
//		.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void should_ProvidesController_WhenGivenWac() throws Exception {

        final ServletContext servletContext = webApplicationContext.getServletContext();
        Assertions.assertNotNull(servletContext);
        Assertions.assertTrue(servletContext instanceof MockServletContext);
    }

    @Test
    void should_PerformSuccessfulGetUsers_WhenValidToken() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users").header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String obtainAccessToken() throws Exception {

        String jsonBody = "{\r\n" + "    \"username\": \"" + TestConstants.USERNAME + "\",\r\n" + "    \"password\": \"" + TestConstants.PASSWORD
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
