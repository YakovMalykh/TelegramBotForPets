-- liquibase formatted sql

-- changeset kew:1

CREATE TABLE IF NOT EXISTS preparation
(
    preparation_id BIGINT PRIMARY KEY,
    preparation_daitingrulespath VARCHAR (255),
    preparation_requiredadoptpath VARCHAR (255),
    preparation_reccomtranstortpath VARCHAR (255),
    preparation_homeimprovementpuppypath VARCHAR (255),
    preparation_homeimprovementdogpath VARCHAR (255),
    preparation_doghandlersadvisepath VARCHAR (255),
    preparation_reccomdoghandlerspath VARCHAR (255),
    preparation_reasonsrefusingpath VARCHAR (255),
    preparation_reccomdisabilitiesgpath VARCHAR (255)
);

-- changeset ymalykh:2
DROP TABLE IF EXISTS preparation;

CREATE TABLE IF NOT EXISTS documentsForPreparation
(
    documentsForPreparation_id SERIAL PRIMARY KEY,
    documentsForPreparation_description VARCHAR (255),
    documentsForPreparation_file_path VARCHAR (255)
);

-- changeset ymalykh:3
DROP TABLE IF EXISTS documents_for_preparation;
DROP TABLE IF EXISTS documentsforpreparation;

CREATE TABLE IF NOT EXISTS documents_for_preparation
(
    documents_for_preparation_id SERIAL PRIMARY KEY,
    documents_for_preparation_description VARCHAR (255),
    documents_for_preparation_file_path VARCHAR (255)
);
-- changeset ymalykh:4
ALTER TABLE documents_for_preparation ADD COLUMN documents_for_preparation_media_type VARCHAR (255);
ALTER TABLE documents_for_preparation ADD COLUMN documents_for_preparation_file_size BIGINT;

