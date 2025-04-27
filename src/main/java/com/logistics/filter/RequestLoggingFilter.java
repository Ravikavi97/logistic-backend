package com.logistics.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Generate or extract requestId
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        
        // Add requestId to MDC
        MDC.put(REQUEST_ID, requestId);
        
        // Wrap request and response to log bodies
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        try {
            // Set requestId in response header
            responseWrapper.setHeader("X-Request-ID", requestId);
            
            // Log request details
            logRequest(requestWrapper);
            
            // Continue with the filter chain
            filterChain.doFilter(requestWrapper, responseWrapper);
            
            // Log response details
            logResponse(responseWrapper);
        } finally {
            // Copy content to the original response
            responseWrapper.copyBodyToResponse();
            // Clear MDC
            MDC.remove(REQUEST_ID);
        }
    }
    
    private void logRequest(ContentCachingRequestWrapper request) {
        String queryString = request.getQueryString();
        String url = queryString == null ? request.getRequestURI() : request.getRequestURI() + "?" + queryString;
        
        log.info("REQUEST: {} {} [RequestId: {}]", 
                request.getMethod(), 
                url, 
                MDC.get(REQUEST_ID));
        
        // Log request headers
        log.debug("Request Headers:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> 
            log.debug("  {}: {}", headerName, request.getHeader(headerName))
        );
        
        // Log request body for POST, PUT, PATCH
        String method = request.getMethod();
        if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
            String requestBody = getRequestBody(request);
            log.info("Request Body: {}", requestBody);
        }
    }
    
    private void logResponse(ContentCachingResponseWrapper response) {
        log.info("RESPONSE: Status={} [RequestId: {}]", 
                response.getStatus(), 
                MDC.get(REQUEST_ID));
        
        // Log response body
        String responseBody = getResponseBody(response);
        log.info("Response Body: {}", responseBody);
    }
    
    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length == 0) return "";
        
        try {
            return new String(buf, request.getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
            return "[Body encoding not supported]";
        }
    }
    
    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length == 0) return "";
        
        try {
            return new String(buf, response.getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
            return "[Body encoding not supported]";
        }
    }
}