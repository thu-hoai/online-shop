package com.example.onlineshop.security.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.onlineshop.entity.Role;
import com.example.onlineshop.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
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

	private String username;

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
		return username;
	}

	public static JwtUser build(User user) {
		List<GrantedAuthority> authorities = user.getAuthorities().stream()
				.map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());

		return new JwtUser(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(),
				user.getPhone(), user.getAddress(), user.getPassword(), user.isEnabled(), authorities);
	}

	public JwtUser(Long id, String userName, String firstName, String lastName, String email, String phone,
			String address, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = userName;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.enabled = enabled;
		this.phone = phone;
		this.address = address;
		this.authorities = (Collection<Role>) authorities;
	}

}
