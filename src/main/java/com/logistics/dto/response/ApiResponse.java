package com.logistics.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiResponse<T> {
    private String status;
    private int code;
    private String message;
    private T data;
    private Metadata metadata;

    public static <T> ApiResponse<T> success(T data, String message, String path, String requestId) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("success");
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        Metadata metadata = new Metadata();
        metadata.setPath(path);
        metadata.setRequestId(requestId);
        response.setMetadata(metadata);
        return response;
    }

    public static <T> ApiResponse<PageResponse<T>> successPage(
            List<T> content,
            long totalElements,
            int pageNumber,
            int pageSize,
            int totalPages,
            String message,
            String path,
            String requestId) {
        ApiResponse<PageResponse<T>> response = new ApiResponse<>();
        response.setStatus("success");
        response.setCode(200);
        response.setMessage(message);
        
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setContent(content);
        pageResponse.setTotalElements(totalElements);
        pageResponse.setPage(pageNumber);
        pageResponse.setSize(pageSize);
        pageResponse.setTotalPages(totalPages);
        pageResponse.setLast(pageNumber == totalPages - 1);
        pageResponse.setFirst(pageNumber == 0);
        pageResponse.setEmpty(content.isEmpty());
        pageResponse.setNumberOfElements(content.size());
        
        response.setData(pageResponse);
        Metadata metadata = new Metadata();
        metadata.setPath(path);
        metadata.setRequestId(requestId);
        response.setMetadata(metadata);
        return response;
    }

    public static <T> ApiResponse<T> error(String message, int code) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus("error");
        response.setCode(code);
        response.setMessage(message);
        response.setMetadata(new Metadata());
        return response;
    }

    @Data
    public static class Metadata {
        private LocalDateTime timestamp;
        private String path;
        private String requestId;

        public Metadata() {
            this.timestamp = LocalDateTime.now();
        }
    }
} 