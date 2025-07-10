package com.docflow.inbox_service.service;

import com.docflow.inbox_service.dto.MessageRequestDto;
import com.docflow.inbox_service.dto.MessageResponseDto;
import com.docflow.inbox_service.dto.MessageSummaryResponseDto;
import com.docflow.inbox_service.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface InboxService {
    boolean createMessage(MessageRequestDto messageRequestDto);

    MessageResponseDto getMessage(String messageId);

    PageResponse<MessageSummaryResponseDto> getAllMessages(String status, Pageable pageable);

    void deleteMessage(List<String> ids);
}

