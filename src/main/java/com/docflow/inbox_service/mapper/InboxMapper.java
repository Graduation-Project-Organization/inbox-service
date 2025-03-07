package com.docflow.inbox_service.mapper;

import com.docflow.inbox_service.config.UserPrincipal;
import com.docflow.inbox_service.dto.MessageRequestDto;
import com.docflow.inbox_service.dto.MessageResponseDto;
import com.docflow.inbox_service.dto.MessageSummaryResponseDto;
import com.docflow.inbox_service.entity.Message;
import org.springframework.security.core.context.SecurityContextHolder;

public class InboxMapper {
    public static MessageSummaryResponseDto mapToMessageSummaryResponseDto(Message message, MessageSummaryResponseDto messageSummaryResponseDto) {
        messageSummaryResponseDto.setMessageId(message.getMessageId());
        messageSummaryResponseDto.setReceiverId(message.getReceiverId());
        messageSummaryResponseDto.setSenderId(message.getSenderId());
        messageSummaryResponseDto.setSenderName(message.getSenderName());
        messageSummaryResponseDto.setSenderEmail(message.getSenderEmail());
        messageSummaryResponseDto.setSubject(message.getSubject());
        messageSummaryResponseDto.setCreatedAt(message.getCreatedAt());
        messageSummaryResponseDto.setUpdatedAt(message.getUpdatedAt());
        return messageSummaryResponseDto;
    }

    public static MessageResponseDto mapToMessageResponseDto(Message message, MessageResponseDto messageResponseDto) {
        messageResponseDto.setMessageId(message.getMessageId());
        messageResponseDto.setReceiverId(message.getReceiverId());
        messageResponseDto.setSenderId(message.getSenderId());
        messageResponseDto.setSenderName(message.getSenderName());
        messageResponseDto.setSenderEmail(message.getSenderEmail());
        messageResponseDto.setSubject(message.getSubject());
        messageResponseDto.setBody(message.getBody());
        messageResponseDto.setCreatedAt(message.getCreatedAt());
        messageResponseDto.setUpdatedAt(message.getUpdatedAt());
        return messageResponseDto;
    }

    public static Message mapToMessage(MessageRequestDto messageRequestDto, Message message) {
        // "getPrincipal()" returns Object type in general as spring security allows different implementations. So, we do casting
        // Since we created a custom UserPrincipal class, we know that the principal is an instance of UserPrincipal.
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        message.setReceiverId(messageRequestDto.getReceiverId());
        message.setSenderId(userPrincipal.getId());
        message.setSenderName(userPrincipal.getUsername());
        message.setSenderEmail(userPrincipal.getEmail());
        message.setSubject(messageRequestDto.getSubject());
        message.setBody(messageRequestDto.getBody());
        return message;
    }
}