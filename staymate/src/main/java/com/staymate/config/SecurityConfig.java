package com.staymate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.staymate.security.CustomUserDetailsService;
import com.staymate.security.GoogleOAuth2SuccessHandler;
import com.staymate.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUsersDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;


    public SecurityConfig(
            CustomUserDetailsService customUsersDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler) {

        this.customUsersDetailsService = customUsersDetailsService;

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

        this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {


        http

            // ================= CSRF =================

            .csrf(csrf -> csrf.disable())


            // ================= DEBUG SECURITY =================

            .exceptionHandling(exception -> exception

                .authenticationEntryPoint(
                    (request, response, authException) -> {

                        System.out.println(
                            "SECURITY BLOCKED: "
                            + request.getMethod()
                            + " "
                            + request.getRequestURI()
                        );


                        response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "Unauthorized"
                        );

                    }
                )

            )


            // ================= USER DETAILS =================

            .userDetailsService(
                customUsersDetailsService
            )


            // ================= AUTHORIZATION =================

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(

                    "/",

                    "/index",

                    "/login",

                    "/register",

                    "/home",

                    "/property/**",
                    
                    "/booking/**",
                    
                    "/bookings",

                    "/api/users/register",

                    "/api/users/login",
                    
                    "/profile",
                    
                    "/owner/**",
                    
                    "/google-role",

                    "/payment-test.html",

                    "/api/payments/verify",

                    "/oauth2/**",

                    "/login/**",

                    "/css/**",

                    "/js/**",

                    "/images/**",

                    "/favicon.ico"

                )

                .permitAll()


                // ================= ADMIN =================

                .requestMatchers(
                    "/api/admin/**"
                )

                .hasRole("ADMIN")


                // ================= EVERYTHING ELSE =================

                .anyRequest()
                .authenticated()

            )


            // ================= GOOGLE OAUTH =================

            .oauth2Login(oauth -> oauth

                .loginPage("/login")

                .successHandler(
                    googleOAuth2SuccessHandler
                )

            )


            // ================= JWT FILTER =================

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );


        return http.build();
    }


    // ================= PASSWORD ENCODER =================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

}