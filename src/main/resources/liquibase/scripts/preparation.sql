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
