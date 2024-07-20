package com.project.movies.aop;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.project.movies.annotataion.RateLimited;
import com.project.movies.exceptions.RateLimitException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class RateLimitingAspect {

    private final CacheManager cacheManager;

    public RateLimitingAspect(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Around("@annotation(com.project.movies.annotataion.RateLimited)")
    public Object rateLimit(ProceedingJoinPoint point) throws Throwable {
        log.info("Running rate limiter AOP!");
        Cache cache = cacheManager.getCache("rateLimitCache");

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        RateLimited rateLimited = method.getAnnotation(RateLimited.class);
        int maxRequests = rateLimited.maxRequests();

        String key = getClientIP() + ":" + method.getName();

        if (cache != null) {
            AtomicInteger reqCount = cache.get(key, AtomicInteger.class);
            if (reqCount == null) {
                reqCount = new AtomicInteger(0);
                cache.put(key, reqCount);
            }

            if (reqCount.incrementAndGet() > maxRequests) {
                throw new RateLimitException("Too many requests, please try again!");
            }
        }

        return point.proceed();
    }

    private String getClientIP() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
