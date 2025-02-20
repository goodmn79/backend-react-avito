--liquibase formatted sql

--changeset goodmn:1

CREATE TABLE images
(
    id         BIGINT PRIMARY KEY,
    path       VARCHAR(32)  NOT NULL,
    size       BIGINT       NOT NULL,
    media_type VARCHAR(128) NOT NULL
);

CREATE TABLE users
(
    id         BIGINT PRIMARY KEY,
    username   VARCHAR(32) UNIQUE NOT NULL,
    password   VARCHAR(16)        NOT NULL,
    email      VARCHAR(128),
    first_name VARCHAR(16)        NOT NULL,
    last_name  VARCHAR(16)        NOT NULL,
    phone      VARCHAR(32),
    role       VARCHAR(8),
    image_id   BIGINT REFERENCES images (id)
);
