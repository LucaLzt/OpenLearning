package com.projects.openlearning.identity.internal.web;

import com.projects.openlearning.identity.internal.service.*;
import com.projects.openlearning.identity.internal.service.dto.*;
import com.projects.openlearning.identity.internal.web.dto.LoginRequest;
import com.projects.openlearning.identity.internal.web.dto.LoginResponse;
import com.projects.openlearning.identity.internal.web.dto.RefreshTokenResponse;
import com.projects.openlearning.identity.internal.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginService loginService;
    private final LogoutService logoutService;
    private final RefreshTokenService refreshTokenService;
    private final RegisterService registerService;
    private final ChangeRoleService changeRoleService;

    @Value("${application.security.jwt.refresh-expiration-seconds}")
    private long refreshTokenDuration;

    @Value("${application.security.cookie-secure}")
    private boolean secureCookie;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        log.info("Received login request for email: {}", request.email());

        // 1. Create LoginCommand from LoginRequest and device info
        var command = new LoginCommand(request.email(), request.password(), httpRequest.getHeader("User-Agent"));

        // 2. Call LoginService to perform login
        LoginResult result = loginService.login(command);

        ResponseCookie cookie = buildCookie(result.refreshToken(), refreshTokenDuration);

        // 3. Return the result as HTTP response
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(result.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        log.info("Received refresh token {} for refresh flow", refreshToken);

        // 1. Call RefreshTokenService to refresh tokens
        RefreshTokenResult result = refreshTokenService.refresh(refreshToken);

        // 2. Build new refresh token cookie
        ResponseCookie cookie = buildCookie(result.refreshToken(), refreshTokenDuration);

        // 3. Return the new access token and refresh token cookie
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new RefreshTokenResponse(result.accessToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refreshToken") String refreshToken) {
        log.info("Received refresh token {} for logout flow", refreshToken);

        // 1. Search for the access token in the Security Context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = null;
        try {
            accessToken = (auth != null && auth.getCredentials() instanceof String) ? (String) auth.getCredentials() : null;
            log.info("Access token from Security Context: {}", accessToken);
        } catch (Exception e) {
            log.warn("Could not extract access token from Security Context: {}", e.getMessage());
        }

        // 2. Call LogoutService to perform logout
        logoutService.logout(new LogoutCommand(
                accessToken,
                refreshToken
        ));

        // 3. Clear the refresh token cookie
        ResponseCookie cookie = buildCookie("", 0);

        // 4. Return no content response with cleared cookie
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        log.info("Received registration request for email: {}", request.email());

        // 1. Call RegisterService to perform registration
        registerService.register(new RegisterCommand(
                request.fullName(),
                request.email(),
                request.password()
        ));

        // 2. Return created response
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Method only for demo purposes to change user role
    @PostMapping("/change-role")
    public ResponseEntity<Void> changeUserRole(@CookieValue(name = "refreshToken") String refreshToken) {
        log.info("Received request to change user role with refresh token: {}", refreshToken);

        // 1. Call ChangeRoleService to change the user's role
        changeRoleService.changeRole(refreshToken);

        // 2. Return ok response
        return ResponseEntity.ok().build();
    }

    // Helper method to build a secure refresh token cookie with appropriate attributes
    private ResponseCookie buildCookie(String token, long maxAge) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(secureCookie)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
    }
}
