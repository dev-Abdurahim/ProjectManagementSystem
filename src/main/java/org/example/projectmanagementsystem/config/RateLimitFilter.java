package org.example.projectmanagementsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projectmanagementsystem.config.securty.SecurityErrorResponder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SecurityErrorResponder errorResponder;

    @Value("${spring.rate-limit.max-requests}")
    private int maxRequests;

    @Value("${spring.rate-limit.window-seconds}")
    private long windowSeconds;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String clientIp = getClientIp(request);
        String key = "rate_limit:" + clientIp;

        // Redis da shu IP uchun nechta so'rov bo'lganini olamiz
        Long requestCount = redisTemplate.opsForValue().increment(key);

        if (requestCount != null && requestCount == 1){
            redisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
            log.info("Rate limit counter boshlandi: {} → IP: {}", key, clientIp);

        }
        if(requestCount != null && requestCount > maxRequests){
            Long ttl = redisTemplate.getExpire(key,TimeUnit.SECONDS);
            long retryAfter = ttl != null ? ttl : windowSeconds;
            log.warn("Rate limit oshib ketdi! IP: {}, Count: {}", clientIp, requestCount);
            errorResponder.sendTooManyRequests(request,response,retryAfter);
            return;

        }
        // Limit oshmagan bosa keyingi filterga otkazamiza
        filterChain.doFilter(request,response);

    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if(xForwardedFor != null && !xForwardedFor.isEmpty()){
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
