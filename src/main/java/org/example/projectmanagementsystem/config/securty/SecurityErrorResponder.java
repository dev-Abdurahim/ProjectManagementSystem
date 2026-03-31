package org.example.projectmanagementsystem.config.securty;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagementsystem.exception.ErrorCode;
import org.example.projectmanagementsystem.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SecurityErrorResponder {

    private final ObjectMapper objectMapper;

    public void sendUnauthorized(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {

        sendError(response,HttpStatus.UNAUTHORIZED,
                ErrorCode.UNAUTHORIZED.name(),
                "Tizimga kirish talab etiladi",
                request.getRequestURI());

    }
    public void sendAccessDenied(HttpServletRequest request, HttpServletResponse response) throws IOException {

        sendError(response,HttpStatus.FORBIDDEN,
                ErrorCode.ACCESS_DENIED.name(),
                "Bu amalni bajarish uchun huquqingiz yetarli emas",
                request.getRequestURI());
    }

    public void sendTooManyRequests(HttpServletRequest request, HttpServletResponse response,long retryAfterSeconds) throws IOException {

        sendError(response,HttpStatus.TOO_MANY_REQUESTS,
                ErrorCode.TOO_MANY_REQUESTS.name(),
                "So'rovlar soni limitdan oshdi." + retryAfterSeconds +  "sekund kutib turing",
                request.getRequestURI());
    }


    private void sendError(HttpServletResponse response,
                           HttpStatus status,
                           String error,
                           String message,
                           String path) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));


    }

}
