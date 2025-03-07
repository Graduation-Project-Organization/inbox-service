package com.docflow.inbox_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@EntityListeners(AuditingEntityListener.class) // to enable audit annotations
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "receiver_id")
    private String receiverId;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "sender_email")
    private String senderEmail;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body")
    private String body;

    @CreatedDate // set the date automatically
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // set the date automatically
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt; // I don't find any importance for it till now

    @Column(name = "is_deleted_by_sender")
    private boolean isDeletedBySender = false;

    @Column(name = "is_deleted_by_receiver")
    private boolean isDeletedByReceiver = false;

}
