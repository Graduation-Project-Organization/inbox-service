package com.docflow.inbox_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

// This class is used in any response except the responses that produced from exceptions

@Schema(name = "Response", description = "schema to hold the information of simple responses")
@Data
@AllArgsConstructor
public class ResponseDto {
    @Schema(description = "Status code of the response")
    private String statusCode;
    @Schema(description = "Status message of the response")
    private String statusMsg;
}
