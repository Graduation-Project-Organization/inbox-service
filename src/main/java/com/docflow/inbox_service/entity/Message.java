package com.docflow.inbox_service.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.time.LocalDateTime;

@Document(collection = "message")
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class Message {
    @Id
    private String messageId;

    private String receiverId;
    private String senderId;
    private String senderName;
    private String senderEmail;
    private String subject;
    private String body;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean isDeletedBySender = false;
    private boolean isDeletedByReceiver = false;
}
