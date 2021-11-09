package com.example.onlineshop.security.dto;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.onlineshop.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an Authenticated User
 */
@Getter
@Setter
@NoArgsConstructor
public class JwtUser implements UserDetails {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String userName;

	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private String address;

	@JsonIgnore
	private String password;

	private boolean enabled;

	private Collection<Role> authorities;

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public String getUsername() {
		return userName;
	}

}
