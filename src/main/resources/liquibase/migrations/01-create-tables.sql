--liquibase formatted sql

--changeset milyaev:create-table-node
CREATE TABLE node
(
  id BIGSERIAL PRIMARY KEY,
  latitude DOUBLE PRECISION,
  longitude DOUBLE PRECISION,
  name TEXT
);
--rollback DROP TABLE node

--changeset milyaev:create-table-tag
CREATE TABLE tag
(
  id BIGSERIAL PRIMARY KEY,
  node BIGINT REFERENCES node(id),
  key TEXT,
  value TEXT
);
--rollback DROP TABLE tag
