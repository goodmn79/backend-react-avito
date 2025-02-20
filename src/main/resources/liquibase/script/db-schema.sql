--liquibase formatted sql

--changeset goodmn:1

CREATE TABLE images
(
    id        BIGINT PRIMARY KEY,
    path      VARCHAR(32),
    size      BIGINT,
    media_type VARCHAR(128)
);

CREATE TABLE users
(
    id         BIGINT PRIMARY KEY,
    username   VARCHAR(32),
    password   VARCHAR(16),
    email      VARCHAR(128),
    first_name VARCHAR(16),
    last_name  VARCHAR(16),
    phone      VARCHAR(32),
    role       VARCHAR(8),
    image_id   BIGINT REFERENCES images (id)
);
