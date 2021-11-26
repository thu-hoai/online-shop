package com.example.onlineshop.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.onlineshop.entity.User;
import com.example.onlineshop.mapper.IUserMapper;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.dto.JwtUser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	private final UserRepository repository;

	private final IUserMapper mapper;

	/**
	 * 1. Load the user from the users table by username. If not found, throw
	 * UsernameNotFoundException. 2. Convert/wrap the user to a UserDetails object
	 * and return it
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(final String username) {

		final User user = repository.findByUsername(username);

		if (user != null) {
			JwtUser jwtUser = mapper.convertToDto(user);
			jwtUser.setAuthorities(user.getAuthorities());
			return jwtUser;
		}
		throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
	}
}
