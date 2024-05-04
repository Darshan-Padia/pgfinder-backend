package com.pgfinder.Controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pgfinder.dto.AuthResponseDto;
import com.pgfinder.dto.LoginDto;
import com.pgfinder.dto.RefreshTokenRequest;
import com.pgfinder.Configuration.JWTGenerator;
import com.pgfinder.Model.Property;
import com.pgfinder.Model.RefreshToken;
import com.pgfinder.Model.Role;
import com.pgfinder.Model.User;
import com.pgfinder.Repository.RoleRepository;
import com.pgfinder.Repository.UserRepository;
import com.pgfinder.Service.CustomUserDetailsService;
import com.pgfinder.Service.PropertyService;
import com.pgfinder.Service.RefreshTokenService;
import com.pgfinder.Service.UserService;
import com.pgfinder.customannotions.AuthRequired;
import com.pgfinder.dto.RegisterDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
// @CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;
    private CustomUserDetailsService customUserDetailService;
    private RefreshTokenService refreshTokenService;
    private UserService userService;
    private PropertyService propertyService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator,
            CustomUserDetailsService customUserDetailService, RefreshTokenService refreshTokenService,
            UserService userService, PropertyService propertyService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.customUserDetailService = customUserDetailService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already taken!");
        }
        if (userRepository.existsByPhone(registerDto.getPhone())) {
            return ResponseEntity.badRequest().body("Error: Phone is already taken!");
        }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Set role for the user
        Role role = roleRepository.findByRoleCode(registerDto.getRoleName());
        if (role == null) {
            return ResponseEntity.badRequest().body("Error: Role '" + registerDto.getRoleName() + "' not found!");
        }
        user.setRole(role);

        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login") // with credentials http only
    public ResponseEntity<?> loginUser(@RequestBody LoginDto LoginDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(LoginDto.getEmail(), LoginDto.getPassword()));
        System.out.println("email");
        System.out.println(LoginDto.getEmail());
        System.out.println("password");
        System.out.println(LoginDto.getPassword());
        // context stores the details of the currently authenticated
        // user and is used to check if a user is authenticated. so the user does not
        // have to log in again

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String role = LoginDto.getRole();
        System.out.println("role");
        System.out.println(role);
        Integer role_id;
        if (role.toLowerCase().equals("owner"))
            role_id = 1;
        else if (role.toLowerCase().equals("tenant"))
            role_id = 2;
        else
            role_id = 3;
        // find user by role_id .. and check if exists if yes then continue else return
        // no user with this role exists

        User user = userRepository.findByEmail(LoginDto.getEmail());
        System.out.println("user");
        System.out.println(user);
        System.out.println("role_id");
        System.out.println(role_id);
        if (user == null || !user.getRole().getRoleCode().equals(LoginDto.getRole()))
            return ResponseEntity.badRequest().body("No user with this role exists");

        // User user = (User) authentication.getPrincipal();
        String token = jwtGenerator.generateJWTToken(authentication);

        // check if already a refresh token exists for this user if yes then delete it
        // and create a new one
        Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByUserId(user.getId());
        if (refreshTokenOpt.isPresent()) {
            refreshTokenService.deleteByUserId(user.getId());
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication.getName());
        String refreshTokenString = refreshToken.getToken();
        System.out.println("refreshTokenString");
        System.out.println(refreshTokenString);
        String bToken = token;
        String cookiename = "accessToken";
        try {
            Cookie cookieee = new Cookie(cookiename, URLEncoder.encode(bToken, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Integer cookieExpiry = 60 * 60 * 24 * 7; // 7 days

        ResponseCookie cookie = ResponseCookie.from("accessToken", bToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookieExpiry)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        ResponseCookie cookiee = ResponseCookie.from("token", refreshTokenString)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookieExpiry)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookiee.toString());

        // setting email as http only cookie
        ResponseCookie cookieee = ResponseCookie.from("email", LoginDto.getEmail())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(cookieExpiry)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieee.toString());
        ResponseCookie cookierole = ResponseCookie.from("role", LoginDto.getRole())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookieExpiry)
                .build();
        ;
        response.addHeader(HttpHeaders.SET_COOKIE, cookierole.toString());

        return ResponseEntity.ok(new AuthResponseDto(role, token, refreshTokenString));

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
        HttpServletResponse response,
        @CookieValue(value = "email", required = false) String email,
        @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            System.out.println("email from logout");
            System.out.println(refreshTokenRequest.getEmail());
            User usr = userService.getUserByEmail(email);
            System.out.println("usr");
            System.out.println(usr);
            Long userId = usr.getId();
            refreshTokenService.deleteByUserId(userId);
            // delete all the cookies
            Integer cookieExpiry = 0; // 0 days
            ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            
            ResponseCookie cookiee = ResponseCookie.from("token", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookiee.toString());
            ResponseCookie cookieee = ResponseCookie.from("email", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookieee.toString());
            ResponseCookie cookierole = ResponseCookie.from("role", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(cookieExpiry)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookierole.toString());


            SecurityContextHolder.clearContext(); // Clear the security context, effectively logging out the user
            return ResponseEntity.ok("Logged out successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/refreshTok")
    // @AuthRequired
    // allow cors
    // @CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true"
    // // ,allow all headers
    // , allowedHeaders = "*"
    // // allow all methods
    // , methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}

    // )
    // allow credentials

    public ResponseEntity<?> refreshTok(
            @CookieValue(value = "token", required = false) String token,
            @CookieValue(value = "role", required = false) String role,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody RefreshTokenRequest 
            refreshTokenRequestDTO) {
        // print all the cookies
        System.out.println("tokennnnn");
        System.out.println(token);

        return refreshTokenService.findByToken(token)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(
                        User -> {
                            Authentication authentication = new UsernamePasswordAuthenticationToken(User.getEmail(),
                                    User.getPassword());

                            String accessToken = jwtGenerator.generateJWTToken(authentication);

                            System.out.println("roleeeeeeeeeee");
                            System.out.println(role);
                            System.out.println(new AuthResponseDto(role, accessToken));
                            // modify the existing cookies
                            Integer cookieExpiry = 60 * 60 * 24 * 7; // 7 days
                            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                                    .httpOnly(true)
                                    .secure(true)
                                    .path("/")
                                    .maxAge(cookieExpiry)
                                    .build();
                            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                            


                            return ResponseEntity
                                    .ok(new AuthResponseDto(role, accessToken, refreshTokenRequestDTO.getEmail()));

                        })
                .orElseThrow(
                        () -> new RuntimeException("Invalid refresh Token"));
    }

    @PostMapping("/refreshToken")
    // @AuthRequired

    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        User usr = userService.getUserByEmail(refreshTokenRequest.getEmail());
        System.out.println("usr");
        System.out.println(usr);
        Long userId = usr.getId();
        return refreshTokenService.findByUserId(userId)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)

                .map(
                        User -> {
                            Authentication authentication = new UsernamePasswordAuthenticationToken(User.getEmail(),
                                    User.getPassword());
                            String accessToken = jwtGenerator.generateJWTToken(authentication);
                            String role = refreshTokenRequest.getRole();
                            System.out.println("roleeeeeeeeeee");
                            System.out.println(role);
                            System.out.println(new AuthResponseDto(role, accessToken));
                            return ResponseEntity
                                    .ok(new AuthResponseDto(role, accessToken, refreshTokenRequest.getEmail()));

                        })
                .orElseThrow(
                        () -> new RuntimeException("Invalid refresh Token"));

    }

    @GetMapping("/checkLoggedIn")
    @AuthRequired
    // if loggedin return true else false
    public ResponseEntity<Boolean> checkLoggedIn() {
        try {
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    
    // creating a test endpoint that requires authorization , only logged in users
    // can access this endpoint also will use the httponly cookie for this
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint(
            @CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        try {
            // Parse the token and extract the user details
            String email = jwtGenerator.getUsernameFromJWTToken(accessToken);
            UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

            // Check if the user is authenticated
            if (userDetails != null && userDetails.isEnabled()) {
                return ResponseEntity.ok("Welcome, " + userDetails.getUsername() + "!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access: " + e.getMessage());
        }

    }
    // @PostMapping
    // @AuthRequired
    // public ResponseEntity<Property> addProperty(
       
    //     @RequestBody Property property) {

    //     Property newProperty = propertyService.saveProperty(property);
    //     return new ResponseEntity<>(newProperty, HttpStatus.CREATED);
    // }

    @GetMapping("/test2")
    @AuthRequired
    public ResponseEntity<String> testEndpoint2() {
        return ResponseEntity.ok("This is a protected endpoint");
    }
}
