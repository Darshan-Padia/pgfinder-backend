package com.pgfinder.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgfinder.Model.Role;
// import com.pgfinder.Model.User;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Role findByRoleCode(String roleCode);
}