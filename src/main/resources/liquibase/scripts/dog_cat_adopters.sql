-- liquibase formatted sql

-- changeset ymalykh:1
ALTER TABLE cat ALTER COLUMN birthday TYPE DATE;
ALTER TABLE dog ALTER COLUMN birthday TYPE DATE;
ALTER TABLE dog_adopter ALTER COLUMN birthday TYPE DATE;
ALTER TABLE cat_adopter ALTER COLUMN birthday TYPE DATE;