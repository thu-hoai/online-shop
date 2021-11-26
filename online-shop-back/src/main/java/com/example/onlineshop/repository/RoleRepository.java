package com.example.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.onlineshop.entity.Role;

public interface RoleRepository  extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

}
