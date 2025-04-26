package com.logistics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private ResponseStatus status;
    private T data;
    private ResponseMetadata metadata;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseStatus {
        private boolean success;
        private int code;
        private String message;
        private String errorCode;
        private String description;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseMetadata {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime timestamp;
        private PageResponse.PaginationMetadata pagination;
        private String path;
        private String requestId;
    }

    private ApiResponse() {
        this.status = new ResponseStatus();
        this.metadata = new ResponseMetadata();
        this.metadata.setTimestamp(LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status.setSuccess(true);
        response.status.setCode(HttpStatus.OK.value());
        response.status.setMessage("Operation completed successfully");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = success(data);
        response.status.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<PageResponse<T>> successPage(Page<T> page) {
        ApiResponse<PageResponse<T>> response = new ApiResponse<>();
        response.status.setSuccess(true);
        response.status.setCode(HttpStatus.OK.value());
        response.status.setMessage("Operation completed successfully");
        response.setData(PageResponse.from(page));
        return response;
    }

    public static <T> ApiResponse<List<T>> successList(List<T> list, String message) {
        ApiResponse<List<T>> response = new ApiResponse<>();
        response.status.setSuccess(true);
        response.status.setCode(HttpStatus.OK.value());
        response.status.setMessage(message);
        response.setData(list);
        return response;
    }

    public static ApiResponse<Void> error(String message, String errorCode, String description, HttpStatus status) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.status.setSuccess(false);
        response.status.setCode(status.value());
        response.status.setMessage(message);
        response.status.setErrorCode(errorCode);
        response.status.setDescription(description);
        return response;
    }

    public static <T> ApiResponse<T> errorWithData(T data, String message, String errorCode, String description, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.status.setSuccess(false);
        response.status.setCode(status.value());
        response.status.setMessage(message);
        response.status.setErrorCode(errorCode);
        response.status.setDescription(description);
        response.setData(data);
        return response;
    }

    public ApiResponse<T> withPath(String path) {
        this.metadata.setPath(path);
        return this;
    }

    public ApiResponse<T> withRequestId(String requestId) {
        this.metadata.setRequestId(requestId);
        return this;
    }
} 