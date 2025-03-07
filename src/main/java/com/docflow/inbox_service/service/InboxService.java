package com.docflow.inbox_service.service;

import com.docflow.inbox_service.dto.MessageRequestDto;
import com.docflow.inbox_service.dto.MessageResponseDto;
import com.docflow.inbox_service.dto.MessageSummaryResponseDto;

import java.util.List;
import java.util.Optional;

public interface InboxService {
    boolean createMessage(MessageRequestDto messageRequestDto);

    MessageResponseDto getMessage(String messageId);

    List<MessageSummaryResponseDto> getAllReceivedMessages();

    List<MessageSummaryResponseDto> getAllSentMessages();

    boolean deleteMessage(String messageId);
}

