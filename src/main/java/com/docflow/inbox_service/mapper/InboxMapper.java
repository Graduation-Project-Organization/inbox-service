package com.docflow.inbox_service.mapper;

import com.docflow.inbox_service.config.UserPrincipal;
import com.docflow.inbox_service.dto.MessageRequestDto;
import com.docflow.inbox_service.dto.MessageResponseDto;
import com.docflow.inbox_service.dto.MessageSummaryResponseDto;
import com.docflow.inbox_service.entity.Message;
import org.mapstruct.Mapper;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(componentModel = "spring")
public abstract class InboxMapper {
    public abstract MessageSummaryResponseDto mapToMessageSummaryResponseDto(Message message);
    public abstract MessageResponseDto mapToMessageResponseDto(Message message);

    public Message mapToMessage(MessageRequestDto messageRequestDto, Message message) {
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