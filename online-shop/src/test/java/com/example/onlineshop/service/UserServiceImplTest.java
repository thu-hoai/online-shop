package com.example.onlineshop.service;

import com.example.onlineshop.TestConstants;
import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.entity.Role;
import com.example.onlineshop.entity.User;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.dto.JwtUser;
import com.example.onlineshop.service.exception.ExistentUserException;
import com.example.onlineshop.service.exception.UserNotFoundException;
import com.example.onlineshop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = UserServiceImpl.class)
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    private UserServiceImpl service;

    User user;

    JwtUser jwtUser;

    UserDto userDto;

    @BeforeEach
    void setUp() {
        // Prepare data
        Set<Role> roles = Set.of(Role.builder().id(TestConstants.ROLE_ID).name(TestConstants.ADMINISTRATOR).build());
        user = User.builder().id(TestConstants.USER_ID).username(TestConstants.USERNAME)
                .password(TestConstants.PASSWORD).authorities(roles).build();
        userDto = UserDto.builder().id(TestConstants.USER_ID).username(TestConstants.USERNAME)
                .password(TestConstants.PASSWORD).authorities(roles).build();
        jwtUser = JwtUser.build(user);
    }

    @Test
    void givenValidUserId_WhenFindById_ThenReturnJwtUser() {
        // given
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        // when
        JwtUser actualResult = service.findUserById(TestConstants.USER_ID);

        // then
        Assertions.assertEquals(TestConstants.USER_ID, actualResult.getId());
        Assertions.assertEquals(TestConstants.USERNAME, actualResult.getUsername());
    }

    @Test
    void givenInvalidUserId_WhenFindUserById_ThrowNonExistentUser() {
        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> service.findUserById(TestConstants.USER_ID));
        Assertions.assertEquals("User not found with id 1", exception.getMessage());
    }

    @Test
    void givenValidDto_WhenCreateUser_ThenReturnUserInfo() {
        // given
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        // when
        JwtUser actualResult = service.createUser(userDto);

        // then
        Assertions.assertEquals(TestConstants.USER_ID, actualResult.getId());
        Assertions.assertEquals(TestConstants.USERNAME, actualResult.getUsername());
    }

    @Test
    void givenInvalidUserId_WhenUserExisted_ThenRejectCreateUser() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        ExistentUserException exp = Assertions.assertThrows(ExistentUserException.class,
                () -> service.createUser(userDto));

        Assertions.assertEquals("User already exists: username1", exp.getMessage());
    }

    @Test
    void whenFindAllUsers_thenReturnJwtUsersList() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<JwtUser> actualResult = service.findAllUsers();

        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void giveValidUserId_WhenUpdateUser_ThenReturnJwtUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        userDto.setEmail("user@gmail.com");
        JwtUser actualResult = service.updateUser(userDto);

        Assertions.assertEquals(userDto.getEmail(), actualResult.getEmail());

    }

    @Test
    void givenInvalidUserId_WhenUpdateUser_ThenThrowUserNotFoundException() {

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> service.updateUser(userDto));

        Assertions.assertEquals("User not found with id 1", exception.getMessage());
    }

    @Test
    void givenValidUserId_WhenDeleteUser_ThenDeleteSuccess() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        service.deleteUserById(user.getId());

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }

    @Test
    void givenInValidUserId_WhenDeleteUser_ThenThrowUserNotFoundException() {
        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> service.deleteUserById(1L));
        Assertions.assertEquals("User not found", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).deleteById(user.getId());
    }
}
