package com.projects.openlearning.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleSpringAuthentication(AuthenticationException e) {
        log.warn("Authentication failure (Spring Security): {}", e.getMessage());
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, "Debes iniciar sesión para acceder a este recurso.",
                "Autenticación Requerida");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleSpringAccessDenied(AccessDeniedException e) {
        log.warn("Access denied (Spring Security): {}", e.getMessage());
        return buildProblemDetail(HttpStatus.FORBIDDEN, "No tienes permisos suficientes para realizar esta acción.",
                "Acceso Denegado");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("Resource Not Found: {}", e.getMessage());
        return buildProblemDetail(HttpStatus.NOT_FOUND, e.getMessage(), "Recurso no encontrado");
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflictException(ConflictException e) {
        log.warn("Business Conflict: {}", e.getMessage());
        return buildProblemDetail(HttpStatus.CONFLICT, e.getMessage(), "Conflicto de negocio");
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ProblemDetail handleForbiddenAccessException(ForbiddenAccessException e) {
        log.warn("Forbidden Access: {}", e.getMessage());
        return buildProblemDetail(HttpStatus.FORBIDDEN, e.getMessage(), "Acceso denegado");
    }

    /**
     * Helper method to build a ProblemDetail response with a consistent structure.
     */
    private ProblemDetail buildProblemDetail(HttpStatus status, String message, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, message);
        problem.setTitle(detail);
        return problem;
    }

}
