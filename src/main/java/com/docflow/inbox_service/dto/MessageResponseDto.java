package com.docflow.inbox_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(name = "MessageResponse", description = "schema to hold the message information that sent when fetching one message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    @Schema(description = "the id of the message", example = "6723ba77-63df-4615-80ed-f96dc41d36a6")
    private String messageId;
    @Schema(description = "the id of the person you want to receive the message", example = "123456789")
    private String receiverId;
    @Schema(description = "the id of the person that sent the message", example = "987654321")
    private String senderId;
    @Schema(description = "the username of the person that sent the message", example = "Mohamed")
    private String senderName;
    @Schema(description = "the email of the person that sent the message", example = "Mohamed@gmail.com")
    private String senderEmail;
    @Schema(description = "the subject of the message", example = "Medical x-rays")
    private String subject;
    @Schema(description = "the body of the message", example = "I reviewed it and everything is ok. Thanks")
    private String body;
    @Schema(description = "the creation date of the message", example = "2025-03-06T01:36:30.29991")
    private LocalDateTime createdAt;
    @Schema(description = "the latest updating date of the message")
    private LocalDateTime updatedAt;
}