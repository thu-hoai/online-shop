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
	/** The clock. */
	private Clock clock = DefaultClock.INSTANCE;

	/** The secret. */
	@Value("${app.jwt.secret}")
	private String secret;

	/** The expiration. */
	@Value("${app.jwt.expiration}")
	private Long expiration;

	/**
	 * Gets the username from token.
	 *
	 * @param token the token
	 * @return the username from token
	 */
	public String getUsernameFromToken(final String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * Gets the issued at date from token.
	 *
	 * @param token the token
	 * @return the issued at date from token
	 */
	public Date getIssuedAtDateFromToken(final String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	/**
	 * Gets the expiration date from token.
	 */
	public Date getExpirationDateFromToken(final String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	/**
	 * Gets the claim from token.
	 */
	public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Gets the all claims from token.
	 */
	private Claims getAllClaimsFromToken(final String token) {

		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * Checks if is token expired.
	 *
	 * @param token the token
	 * @return the boolean
	 */
	private Boolean isTokenExpired(final String token) {
		final Date expirer = getExpirationDateFromToken(token);
		return expirer.before(clock.now());
	}

	/**
	 * Checks if is created before last password reset.
	 */
	private Boolean isCreatedBeforeLastPasswordReset(final Date created, final Date lastPasswordReset) {
		return lastPasswordReset != null && created.before(lastPasswordReset);
	}

	/**
	 * Ignore token expiration.
	 */
	private Boolean ignoreTokenExpiration(final String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	/**
	 * Generate token.
	 */
	public String generateToken(final UserDetails userDetails) {
		final Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	/**
	 * Do generate token.
	 */
	private String doGenerateToken(final Map<String, Object> claims, final String subject) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * Can token be refreshed.
	 */
	public Boolean canTokenBeRefreshed(final String token, final Date lastPasswordReset) {
		final Date created = getIssuedAtDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	/**
	 * Refresh token.
	 */
	public String refreshToken(final String token) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * Validate token.
	 */
	public Boolean validateToken(final String token, final UserDetails userDetails) {
		final JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);

		return username.equals(user.getUsername()) && !isTokenExpired(token);
	}

	/**
	 * Calculate expiration date.
	 *
	 * @param createdDate the created date
	 * @return the date
	 */
	private Date calculateExpirationDate(final Date createdDate) {
		return new Date(createdDate.getTime() + expiration * 1000);
	}

}
