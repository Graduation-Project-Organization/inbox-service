package com.docflow.inbox_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "MessageRequest", description = "schema to hold the message information that sent when message creation")
@Data
@NoArgsConstructor @AllArgsConstructor
public class MessageRequestDto {
    @Schema(description = "the id of the person you want to receive the message", example = "123456789")
    @NotBlank(message = "receiver ID cannot be null or empty")
    private String receiverId;

    @Schema(description = "the subject of the message", example = "Medical x-rays")
    @NotBlank(message = "subject cannot be null or empty")
    @Size(min = 3, max = 255, message = "the subject should be between 3 and 255 chars")
    private String subject;

    @Schema(description = "the body of the message", example = "I reviewed it and everything is ok. Thanks")
    @NotBlank(message = "body cannot be null or empty")
    @Size(min = 15, message = "the body cannot be lower then 15 chars")
    private String body;
}
