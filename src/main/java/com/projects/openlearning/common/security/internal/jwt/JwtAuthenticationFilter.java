package com.projects.openlearning.common.security.internal.jwt;

import com.projects.openlearning.common.security.api.TokenParserPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenParserPort tokenParserPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);

        // 1. Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract the token from the header
        String token = authHeader.substring(BEARER_PREFIX.length());

        // 3. Validate the token
        // * Signature, expiration, etc.
        if (!tokenParserPort.isAccessTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 5. If the token is valid, set the authentication in the SecurityContext
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            var authentication = tokenParserPort.getAuthentication(token);

            // Set the authentication details from the request
            if (authentication instanceof AbstractAuthenticationToken authToken) {
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
