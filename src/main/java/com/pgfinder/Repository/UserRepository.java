package com.pgfinder.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pgfinder.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    User findByRoleId(Integer roleId);
    User  findByUsername(String username);

    User  findByEmail(String email);

    User  findByPhone(String phone);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);


}