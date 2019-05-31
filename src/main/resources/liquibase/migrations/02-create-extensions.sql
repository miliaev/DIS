--liquibase formatted sql

--changeset milyaev:create-extensions
CREATE EXTENSION cube;
CREATE EXTENSION earthdistance;
