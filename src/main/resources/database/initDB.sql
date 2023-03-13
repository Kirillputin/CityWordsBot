CREATE TABLE IF NOT EXISTS city
(
    id   SERIAL PRIMARY KEY,
    city VARCHAR(200) NOT NULL
        );
-- DROP TABLE IF EXISTS city

CREATE TABLE IF NOT EXISTS user_profile
(
    id   SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    scope     INTEGER
    );

CREATE TABLE IF NOT EXISTS word_except
(
    id   SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    city_name VARCHAR(200) NOT NULL
    );