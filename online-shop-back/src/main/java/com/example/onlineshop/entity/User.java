package com.example.onlineshop.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name", length = 50)
	private String username;

	@Column(name = "first_name", length = 255)
	private String firstName;

	@Column(name = "last_name", length = 255)
	private String lastName;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "user_password", length = 255)
	private String password;

	@Column(name = "user_enabled")
	private boolean enabled;

	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "user_address", length = 255)
	private String address;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> authorities = new HashSet<>();

}
