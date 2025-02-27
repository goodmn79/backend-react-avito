--liquibase formatted sql

--changeset goodmn:1

CREATE TABLE IF NOT EXISTS images
(
    id         INT PRIMARY KEY,
    path       VARCHAR(32)  NOT NULL,
    size       BIGINT       NOT NULL,
    media_type VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id         INT PRIMARY KEY,
    username   VARCHAR(32) UNIQUE NOT NULL,
    password   VARCHAR(16)        NOT NULL,
    email      VARCHAR(128),
    first_name VARCHAR(16)        NOT NULL,
    last_name  VARCHAR(16)        NOT NULL,
    phone      VARCHAR(32),
    role       VARCHAR(8),
    image_id   INT REFERENCES images (id)
);

CREATE TABLE IF NOT EXISTS ads
(
    pk          INT PRIMARY KEY,
    price       INT         NOT NULL,
    title       VARCHAR(32) NOT NULL,
    description VARCHAR(64),
    author_id   INT REFERENCES users (id),
    image_id    INT REFERENCES images (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    pk        INT PRIMARY KEY,
    crated_at BIGINT      NOT NULL,
    text      VARCHAR(32) NOT NULL,
    author_id INT REFERENCES users (id),
    ad_pk     INT REFERENCES ads (pk)
);

