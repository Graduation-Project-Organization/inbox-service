CREATE DATABASE IF NOT EXISTS inbox;

CREATE TABLE IF NOT EXISTS message (
    message_id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(), -- UUID stored as VARCHAR
    receiver_id VARCHAR(36) NOT NULL,
    sender_id VARCHAR(36) NOT NULL,
    sender_name VARCHAR(255) NOT NULL,
    sender_email VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    is_deleted_by_sender BOOLEAN NOT NULL DEFAULT FALSE,
    is_deleted_by_receiver BOOLEAN NOT NULL DEFAULT FALSE
    );