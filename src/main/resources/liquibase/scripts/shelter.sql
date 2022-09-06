-- liquibase formatted sql

-- changeset kew:1

CREATE TABLE IF NOT EXISTS shelter
(
    shelter_id BIGINT PRIMARY KEY,
    shelter_name VARCHAR (40),
    shelter_adress VARCHAR(255),
    shelter_mappach VARCHAR(255),
    shelter_recomendationpach VARCHAR(255),
    shelter_schedule VARCHAR(255),
    shelter_specification VARCHAR(255),
    shelter_description VARCHAR(255)
);
-- changeset ymalykh:2
ALTER TABLE shelter RENAME COLUMN shelter_mappach to shelter_mappath;
ALTER TABLE shelter RENAME COLUMN shelter_recomendationpach to shelter_recomendationpath;
ALTER TABLE shelter RENAME COLUMN shelter_specification to shelter_specialization;
