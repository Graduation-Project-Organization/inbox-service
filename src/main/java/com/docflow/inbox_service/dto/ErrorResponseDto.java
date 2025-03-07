package com.docflow.inbox_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

// This class is used especially for error responses that result from exceptions.

@Schema(name = "ErrorResponse", description = "schema to hold error response information")
@Data
@AllArgsConstructor
public class ErrorResponseDto {
    @Schema(description = "API path invoked by the client")
    private String apiPath;
    @Schema(description = "the error message representing the error message")
    private String errorMsg;
    @Schema(description = "the error code representing the error happened")
    private HttpStatus errorCode;
    @Schema(description = "time representing when the error happened")
    private LocalDateTime errorTime;
}
