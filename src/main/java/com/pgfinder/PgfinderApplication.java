package com.pgfinder;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pgfinder.Model.Owner;
import com.pgfinder.Model.User;
import com.pgfinder.Repository.UserRepository;
import com.pgfinder.Model.Role;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackages = {"com.pgfinder.Model"}) // Specify the package where your entity classes are located
public class PgfinderApplication {

   

   
    public static void main(String[] args) {
        
        SpringApplication.run(PgfinderApplication.class, args);
    }

    // @PostConstruct
    // private void init(){
    //    List <Authority> authorities = new ArrayList<>();
    //      authorities.add(createAuthority("OWNER", "owner Role"));
    //         authorities.add(createAuthority("TENANT", "tenant Role"));
    //     User user = new User();
    //     user.setUsername("DarshanPadia");
    //     user.setPassword(passwordEncoder.encode("123"));
    //     user.setRole("OWNER");
    //     user.setAuthorities(authorities);
        
    //     userDetailRepository.save(user);
    // }
   

   
}
