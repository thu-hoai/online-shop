package com.example.onlineshop.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
	/** The user details service. */
	@Autowired
	private UserDetailsService userDetailsService;

	/** The jwt token provider */
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/** The token header. */
	@Value("${app.jwt.header}")
	private String tokenHeader;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain chain) throws ServletException, IOException {
		log.debug("Processing authentication for '{}'", request.getRequestURL());

		// Read JWT authentication token from the request
		final String requestHeader = request.getHeader(tokenHeader);
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			final String authToken = requestHeader.substring(7);
			final String username = getUserName(authToken);

			log.debug("Checking authentication for user '{}'", username);

			/* Validate token, set Security context */
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				log.debug("Security context was null, so authorizating user");

				final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(authToken, userDetails))) {
					final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					log.info("Authorizated user '{}', setting security context", username);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} else {
			log.warn("couldn't find bearer string, will ignore the header");
		}

		chain.doFilter(request, response);
	}

	/**
	 * Gets the username from the authentication token.
	 *
	 * @param authToken authentication token
	 * @return the username
	 */
	private String getUserName(final String authToken) {
		try {
			return jwtTokenUtil.getUsernameFromToken(authToken);
		} catch (final IllegalArgumentException e) {
			log.error("An error occured during getting username from token", e);
		} catch (final ExpiredJwtException e) {
			log.warn("The token is expired and not valid anymore", e);
		}

		return null;
	}
}
