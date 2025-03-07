package com.docflow.inbox_service.service;

import com.docflow.inbox_service.config.UserPrincipal;
import com.docflow.inbox_service.dto.MessageRequestDto;
import com.docflow.inbox_service.dto.MessageResponseDto;
import com.docflow.inbox_service.dto.MessageSummaryResponseDto;
import com.docflow.inbox_service.entity.Message;
import com.docflow.inbox_service.exception.MessageNotFound;
import com.docflow.inbox_service.exception.UserNotFound;
import com.docflow.inbox_service.exception.UserNotHavePermission;
import com.docflow.inbox_service.mapper.InboxMapper;
import com.docflow.inbox_service.repository.MessageRepository;
import lombok.AllArgsConstructor;
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

    @Override
    public boolean createMessage(MessageRequestDto messageRequestDto) {
        // check the receiver id is valid or not.
        if (!checkReceiverId(messageRequestDto.getReceiverId())) {
            throw new UserNotFound(messageRequestDto.getReceiverId());
        }

        // map the request message to actual message [the rest data is handled in the mapper]
        Message newMessage = InboxMapper.mapToMessage(messageRequestDto, new Message());

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
            MessageResponseDto messageResponseDto = InboxMapper.mapToMessageResponseDto(message, new MessageResponseDto());
            return messageResponseDto;
        } else {
            throw new UserNotHavePermission(String.format("You don't have the permissions to get this message %s", messageId));
        }
    }

    @Override
    public List<MessageSummaryResponseDto> getAllReceivedMessages() {
        // catch the user id
        String userId = getCurrentUserId();
        // get all received messages
        List<Message> allReceivedMessages = messageRepository.findAllByReceiverIdAndIsDeletedByReceiverFalseOrderByCreatedAtDesc(userId);
        // map them to MessageResponseDto object
        List<MessageSummaryResponseDto> allReceivedMessagesDto = new ArrayList<>();
        if (allReceivedMessages != null) { // because stream may throw an error if the list empty
            allReceivedMessagesDto = allReceivedMessages.stream()
                    .map(message -> InboxMapper.mapToMessageSummaryResponseDto(message, new MessageSummaryResponseDto()))
                    .collect(Collectors.toList());
        }
        return allReceivedMessagesDto;
    }

    @Override
    public List<MessageSummaryResponseDto> getAllSentMessages() {
        // get the current user id
        String userId = getCurrentUserId();
        // fetch all sent messages of the current user
        List<Message> allSentMessages = messageRepository.findAllBySenderIdAndIsDeletedBySenderFalseOrderByCreatedAtDesc(userId);
        // map the message object to messageResponseDto object
        List<MessageSummaryResponseDto> allSentMessagesDto = new ArrayList<>();
        if (allSentMessages != null) { // because stream may throw an error if the list empty
            allSentMessagesDto = allSentMessages.stream()
                    .map(message -> InboxMapper.mapToMessageSummaryResponseDto(message, new MessageSummaryResponseDto()))
                    .collect(Collectors.toList());
        }
        return allSentMessagesDto;
    }

    @Override
    public boolean deleteMessage(String messageId) {
        // fetch the user id
        String userId = getCurrentUserId();

        // check the message exist or not
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFound(messageId));

        // check the user has the permission to delete the image or not, and delete the image
        if (userId.equals(message.getSenderId()) && !(message.isDeletedBySender())) {
            message.setDeletedBySender(true);
            messageRepository.save(message);
        } else if (userId.equals(message.getReceiverId()) && !(message.isDeletedByReceiver())) {
            message.setDeletedByReceiver(true);
            messageRepository.save(message);
        } else {
            throw new UserNotHavePermission(String.format("You don't have the permissions to delete this message %s", messageId));
        }

        // delete the image ever if the both sides deleted it
        if (message.isDeletedBySender() && message.isDeletedByReceiver()) {
            messageRepository.deleteById(messageId);
        }
        return true;
    }

    private String getCurrentUserId() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    // ** The logic of this method is not written till now
    private boolean checkReceiverId(String receiverId) {
        return true;
    }
}

// "1234567789"
// Anything now