package com.docflow.inbox_service.service;

import com.docflow.inbox_service.config.UserPrincipal;
import com.docflow.inbox_service.dto.MessageRequestDto;
import com.docflow.inbox_service.dto.MessageResponseDto;
import com.docflow.inbox_service.dto.MessageSummaryResponseDto;
import com.docflow.inbox_service.dto.PageResponse;
import com.docflow.inbox_service.entity.Message;
import com.docflow.inbox_service.exception.MessageNotFound;
import com.docflow.inbox_service.exception.UserNotFound;
import com.docflow.inbox_service.exception.UserNotHavePermission;
import com.docflow.inbox_service.mapper.InboxMapper;
import com.docflow.inbox_service.mapper.PageMapper;
import com.docflow.inbox_service.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InboxServiceImpl implements InboxService {
    private MessageRepository messageRepository;
    private PageMapper pageMapper;
    private InboxMapper inboxMapper;

    @Override
    public boolean createMessage(MessageRequestDto messageRequestDto) {
        // check the receiver id is valid or not.
        if (!checkReceiverId(messageRequestDto.getReceiverId())) {
            throw new UserNotFound(messageRequestDto.getReceiverId());
        }

        // map the request message to actual message [the rest data is handled in the mapper]
        Message newMessage = inboxMapper.mapToMessage(messageRequestDto, new Message());

        // save the message
        messageRepository.save(newMessage);
        return true;
    }

    @Override
    public MessageResponseDto getMessage(String messageId) {
        // get the user id
        String userId = getCurrentUserId();

        // check the message exist or not
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFound(messageId));

        // check the user has the permissions to see the message.
        if ((userId.equals(message.getReceiverId())&& !(message.isDeletedByReceiver())) || (userId.equals(message.getSenderId()) && !(message.isDeletedBySender()))) {
            // map the message and return it
            MessageResponseDto messageResponseDto = inboxMapper.mapToMessageResponseDto(message);
            return messageResponseDto;
        } else {
            throw new UserNotHavePermission(String.format("You don't have the permissions to get this message %s", messageId));
        }
    }

    @Override
    public PageResponse<MessageSummaryResponseDto> getAllMessages(String status, Pageable pageable) {
        // fetch the current user id
        String userId = getCurrentUserId();

        // retrieve messages
        Page<MessageSummaryResponseDto> messages;
        if (status.equals("sent")) {
            messages = messageRepository.findAllBySenderIdAndIsDeletedBySenderFalse(userId, pageable).map(inboxMapper::mapToMessageSummaryResponseDto);
        } else {
            messages = messageRepository.findAllByReceiverIdAndIsDeletedByReceiverFalse(userId, pageable).map(inboxMapper::mapToMessageSummaryResponseDto);
        }

        // return messages
        return pageMapper.toPageResponse(messages);
    }

    @Override
    public void deleteMessage(List<String> ids) {
        // fetch user id
        String userId = getCurrentUserId();

        // fetch messages
        List<Message> messages = messageRepository.findAllById(ids);

        // validate the current user has the permissions to delete the message or not
        for (Message message : messages) { // apply validation logic
            if (userId.equals(message.getSenderId()) && !(message.isDeletedBySender())) {
                message.setDeletedBySender(true); // soft deletion
                messageRepository.save(message);
            } else if (userId.equals(message.getReceiverId()) && !(message.isDeletedByReceiver())) {
                message.setDeletedByReceiver(true); // soft deletion
                messageRepository.save(message);
            } else {
                throw new UserNotHavePermission(String.format("You don't have the permissions to delete this message %s", message.getMessageId()));
            }
        }
    }

    private String getCurrentUserId() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    // ** The logic of this method is not written till now
    private boolean checkReceiverId(String receiverId) {
        return true;
    }
}