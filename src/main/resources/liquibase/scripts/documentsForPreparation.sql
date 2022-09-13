-- liquibase formatted sql

-- changeset ymalykh:1
CREATE TABLE IF NOT EXISTS documents_for_preparation
(
    documents_for_preparation_id SERIAL PRIMARY KEY,
    documents_for_preparation_description VARCHAR (255),
    documents_for_preparation_file_path VARCHAR (255),
    documents_for_preparation_media_type VARCHAR (255),
    documents_for_preparation_file_size BIGINT,
    documents_for_preparation_kind_of_animal VARCHAR (255)
);

