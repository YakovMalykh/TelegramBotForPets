-- liquibase formatted sql

-- changeset kew:1

CREATE TABLE IF NOT EXISTS doghandler
(
    doghandler_id BIGSERIAL PRIMARY KEY,
    doghandler_chatid BIGINT,
    doghandler_name VARCHAR (20),
    doghandler_middlename VARCHAR (20),
    doghandler_lastname VARCHAR (20),
    doghandler_gender VARCHAR (1),
    doghandler_datebirth DATE,
    doghandler_telephone VARCHAR (20),
    doghandler_adress VARCHAR (255),
    doghandler_description VARCHAR (255)
);

