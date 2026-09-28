package com.staymate.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.staymate.entity.AppUser;
import com.staymate.entity.Role;
import com.staymate.repository.AppUserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public GoogleOAuth2SuccessHandler(
            AppUserRepository appUserRepository,
            JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User =
                (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        AppUser appUser = appUserRepository
                .findByEmail(email)
                .orElse(null);

        // Existing Google user
        if (appUser != null) {

            String token = jwtService.generateToken(appUser.getEmail());

            if (appUser.getRole() == Role.OWNER) {
                response.sendRedirect("/owner?token=" + token);
            } else {
                response.sendRedirect("/home?token=" + token);
            }

            return;
        }

        // New Google user
        request.getSession().setAttribute("googleEmail", email);
        request.getSession().setAttribute("googleName", name);

        response.sendRedirect("/google-role");
    }
}