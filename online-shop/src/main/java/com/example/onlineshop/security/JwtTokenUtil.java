package com.example.onlineshop.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.onlineshop.security.dto.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtil {

	private Clock clock = DefaultClock.INSTANCE;

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration}")
	private int jwtExpiration;

	/**
	 * Generate JWT token
	 */
	public String generateJwtToken(final UserDetails userDetails) {

		final Map<String, Object> claims = new HashMap<>();

		final Date createdDate = clock.now();
		final Date expirationDate = calculateExprirationData(createdDate);

		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				//
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public Boolean validateToken(final String token, final UserDetails userDetails) {
		final JwtUser user = (JwtUser) userDetails;
		final String userName = getUsernameFromToken(token);
		return userName.equals(user.getUsername()) && !isTokenExpired(token);
	}

	public String getUsernameFromToken(String authToken) {
		return getClaimFromToken(authToken, Claims::getSubject);
	}

	private boolean isTokenExpired(String token) {
		final Date exprireDate = getClaimFromToken(token, Claims::getExpiration);
		return exprireDate.before(clock.now());
	}

	/**
	 * Parsing clainms
	 * 
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
		final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claimsResolver.apply(claims);
	}

	private Date calculateExprirationData(final Date createdDate) {
		return new Date(createdDate.getTime() + jwtExpiration * 1000);
	}

}
