package com.example.onlineshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.onlineshop.security.JwtAuthenticationEntryPoint;
import com.example.onlineshop.security.JwtAuthorizationTokenFilter;
import com.example.onlineshop.security.JwtUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableTransactionManagement
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/*
	 * Use the BCrypt password hashing function (Spring Security’s default) for all
	 * passwords
	 */
	@Bean
	public PasswordEncoder passwordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	// Specify a UserDetailsService for having access to the user’s password
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	// Custom JWT based security filter
	@Autowired
	JwtAuthorizationTokenFilter authenticationTokenFilter;

	@Value("${app.jwt.header}")
	private String tokenHeader;

	@Value("${app.jwt.route-authentication-path}")
	private String authenticationPath;

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoderBean());
	}

	// Configure the FilterChain
	@Override
	protected void configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// we don't need CSRF because our token is invulnerable
				.csrf().disable()

				.cors().and()

				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

				// don't create session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				.authorizeRequests()

				// Permit all /auth/** and /h2-console/**/**
				.antMatchers("/swagger-ui/**").permitAll()
				// .antMatchers("/auth/**", "/oauth2/**").permitAll().

				.antMatchers("/auth/**", "/oauth2/**").permitAll().

				// Apart from /auth/** and /h2-console/**/*, any other request needs the user to
				// be authenticated first
				anyRequest().authenticated();

		// Add custom Token based authentication filter
		httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		// disable page caching
		httpSecurity.headers().frameOptions().sameOrigin() // required to set for H2 else H2 Console will be blank.
				.cacheControl();
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		// AuthenticationTokenFilter will ignore the below paths
		web.ignoring()

				// allow anonymous resource requests
				.and().ignoring().antMatchers(HttpMethod.GET, "/h2-console/**", "/swagger-ui/**")

				// Un-secure H2 Database (for testing purposes, H2 console shouldn't be
				// unprotected in production)
				.and().ignoring().antMatchers("/h2-console/**/**");
	}

}
