package com.docflow.inbox_service.repository;

import com.docflow.inbox_service.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Page<Message> findAllByReceiverIdAndIsDeletedByReceiverFalse(String receiverId, Pageable pageable);
    Page<Message> findAllBySenderIdAndIsDeletedBySenderFalse(String senderId, Pageable pageable);
}
