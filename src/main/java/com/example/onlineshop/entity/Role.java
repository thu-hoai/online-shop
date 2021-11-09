package com.example.onlineshop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "role")
@EqualsAndHashCode(of = "id")
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "")
    private String name;
    private String description;
    @Override
    public String getAuthority() {
        return name;
    }
}
