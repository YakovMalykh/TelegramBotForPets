-- liquibase formatted sql

-- changeset sascvotch:1
CREATE TABLE IF NOT EXSIST breeds
(
    breed_id   BIGSERIAL PRIMARY KEY,
    breed_name VARCHAR (100),
);
CREATE TABLE IF NOT EXSIST pets
(
    pet_id   BIGSERIAL PRIMARY KEY,
    pet_name VARCHAR (20),
    pet_date_birth DATE,
    pet_gender VARCHAR(20),
    breed_id BIGINT REFERENCES breeds(breed_id) ,
    pet_neutered BOOLEAN,
    pet_special_care BOOLEAN
);
