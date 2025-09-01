package com.example.INGStoreManagement.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    private String title;
    private HttpStatus status;
    private String detail;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> fields;
}
