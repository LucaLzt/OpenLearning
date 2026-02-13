package com.projects.openlearning.common.security.internal.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final ProxyManager<String> proxyManager;

    @Value("${application.security.rate-limit.capacity}")
    private int capacity;

    @Value("${application.security.rate-limit.refillAmount}")
    private int refillAmount;

    @Value("${application.security.rate-limit.refill-period-minutes}")
    private int refillPeriodMinutes;

    /**
     * List of public endpoints that are subject to rate limiting.
     * This list are is hardcoded as a demonstration for the demo.
     */
    private final List<String> publicEndpoints = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/refresh"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Check if the request URI matches any of the public endpoints
        String path = request.getRequestURI();

        // 2. If it matches, apply rate limiting based on client IP
        if (publicEndpoints.contains(path)) {
            String ip = getClientIp(request);
            String key = "rate-limit:" + ip;

            Bucket bucket = resolveBucket(key);

            if (!bucket.tryConsume(1)) {
                log.warn("Rate limit exceeded for IP: {}", ip);
                handleRateLimitExceeded(request, response);
                return;
            }
        }
        // 3. If not rate limited, continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Resolves the bucket for the given key (IP address).
     * If the bucket does not exist, it will be created with the specified configuration.
     */
    private Bucket resolveBucket(String key) {
        Supplier<BucketConfiguration> configSupplier = () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(capacity)
                        .refillIntervally(refillAmount, Duration.ofMinutes(refillPeriodMinutes))
                        .build()
                )
                .build();
        return proxyManager.builder().build(key, configSupplier);
    }

    /**
     * Handles the response when the rate limit is exceeded.
     * It sets the appropriate HTTP status and returns a JSON response with details.
     */
    private void handleRateLimitExceeded(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.TOO_MANY_REQUESTS,
                "Too many requests. Please try again later."
        );

        problem.setTitle("Rate limit exceeded");
        problem.setInstance(URI.create(request.getRequestURI()));

        response.getWriter().write(objectMapper.writeValueAsString(problem));
    }

    /**
     * Extracts the client's IP address from the request, considering possible proxy headers.
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
