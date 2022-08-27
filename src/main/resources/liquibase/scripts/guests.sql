-- liquibase formatted sql

-- changeset ymalykh:1

CREATE TABLE guests
(
    chat_id   BIGINT PRIMARY KEY,
    user_name VARCHAR (100),
    phone_number VARCHAR(20)
);

