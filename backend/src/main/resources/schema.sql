-- Manual schema creation for H2 2.x compatibility
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE cars (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    car_year INT NOT NULL,
    license_plate VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(255),
    user_id BIGINT NOT NULL,
    CONSTRAINT FK_cars_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
