package com.logistics.util;

import org.slf4j.MDC;

public class RequestContext {
    private static final String REQUEST_ID = "requestId";
    
    /**
     * Get the current request ID from MDC
     */
    public static String getRequestId() {
        return MDC.get(REQUEST_ID);
    }
    
    /**
     * Set a request ID in MDC
     */
    public static void setRequestId(String requestId) {
        MDC.put(REQUEST_ID, requestId);
    }
    
    /**
     * Clear the request ID from MDC
     */
    public static void clearRequestId() {
        MDC.remove(REQUEST_ID);
    }
}