package com.pgfinder.Service;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pgfinder.Model.Owner;
import com.pgfinder.Model.Tenant;
import com.pgfinder.Model.User;
import com.pgfinder.Repository.OwnerRepository;
import com.pgfinder.Repository.TenantRepository;
import com.pgfinder.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userDetailRepository;

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Autowired
    // private OwnerRepository ownerRepository;

    // @Autowired
    // private TenantRepository tenantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userDetailRepository.findByEmail(email);

        if (user != null) {
            System.out.println(user);
            System.out.println(user.getEmail());
            System.out.println(passwordEncoder().encode(user.getPassword()));
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().toString())
                    .build();
            return userDetails;
        }

        throw new UsernameNotFoundException("User not found");

    }
}