package com.pgfinder.customannotions;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.pgfinder.Configuration.JWTGenerator;
import com.pgfinder.Service.CustomUserDetailsService;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTGenerator jwtGenerator;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if (handlerMethod.getMethod().isAnnotationPresent(AuthRequired.class)) {
                Cookie[] cookies = request.getCookies();
                if (cookies == null) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
                    return false;
                }

                String accessToken = null;
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("accessToken")) {
                        accessToken = cookie.getValue();
                        break;
                    }
                }

                if (accessToken == null ) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
                    return false;
                }

                // String token = accessToken.substring(7);
                String email = jwtGenerator.getUsernameFromJWTToken(accessToken);
                if (email == null || !customUserDetailsService.loadUserByUsername(email).isEnabled()) {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
                    return false;
                }
            }
        }
        return true;
    }
}