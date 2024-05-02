// package com.pgfinder.Configuration;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

// @Configuration
// @EnableWebSecurity
// @Profile("!https")
// public class SecSecurityConfig {

//     //...

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//       return http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//           .authorizeHttpRequests(request -> request.requestMatchers("/admin/**")
//             .hasRole("ADMIN")
//             .requestMatchers("/anonymous*")
//             .anonymous()
//             .requestMatchers(HttpMethod.GET, "/index*", "/static/**", "/*.js", "/*.json", "/*.ico", "/rest")
//             .permitAll()
//             .anyRequest()
//             .authenticated())
//           .formLogin(form -> form.loginPage("/index.html")
//             .loginProcessingUrl("/perform_login")
//             .defaultSuccessUrl("/homepage.html", true)
//             .failureUrl("/index.html?error=true"))
//           .logout(logout -> logout.logoutUrl("/perform_logout")
//             .deleteCookies("JSESSIONID"))
//           .build();
//     }
// }