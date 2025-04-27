package com.logistics.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("within(com.logistics.controller..*)")
    public void controllerPointcut() {}

    @Pointcut("within(com.logistics.service..*)")
    public void servicePointcut() {}

    @Pointcut("within(com.logistics.repository..*)")
    public void repositoryPointcut() {}

    @Around("controllerPointcut() || servicePointcut() || repositoryPointcut()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        String requestId = MDC.get("requestId");
        
        // Log method entry with parameters
        try {
            String params = Arrays.toString(joinPoint.getArgs());
            log.info("[{}] Entering: {}.{}() with parameters: {}", 
                    requestId, className, methodName, params);
        } catch (Exception e) {
            log.info("[{}] Entering: {}.{}() with parameters: [Failed to serialize]", 
                    requestId, className, methodName);
        }
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            // Execute the method
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            // Log exception
            log.error("[{}] Exception in {}.{}() with error: {}", 
                    requestId, className, methodName, e.getMessage(), e);
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log method exit with result
            try {
                String resultStr = (result != null) ? objectMapper.writeValueAsString(result) : "null";
                log.info("[{}] Exiting: {}.{}() with result: {} in {}ms", 
                        requestId, className, methodName, resultStr, executionTime);
            } catch (Exception e) {
                log.info("[{}] Exiting: {}.{}() with result: [Failed to serialize] in {}ms", 
                        requestId, className, methodName, executionTime);
            }
        }
    }
}