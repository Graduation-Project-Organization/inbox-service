package com.docflow.inbox_service.repository;

import com.docflow.inbox_service.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByReceiverIdAndIsDeletedByReceiverFalseOrderByCreatedAtDesc(String receiverId);
    List<Message> findAllBySenderIdAndIsDeletedBySenderFalseOrderByCreatedAtDesc(String senderId);
}
