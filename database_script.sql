-- ==========================================================
-- SQL Server Script for Ufinet Autos Challenge
-- Purpose: Initialize the database schema and preload initial data.
-- docker start sqlserver-autos
-- ==========================================================

-- 1. Create Users Table
-- This table stores user credentials for authentication.
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY, -- Unique identifier for each user, auto-incremented.
    username VARCHAR(255) NOT NULL UNIQUE, -- Unique username used for login.
    password VARCHAR(255) NOT NULL -- BCrypt encoded password.
);

-- 2. Create Cars (Autos) Table
-- This table stores information about the vehicles owned by users.
CREATE TABLE cars (
    id BIGINT IDENTITY(1,1) PRIMARY KEY, -- Unique identifier for each car, auto-incremented.
    brand VARCHAR(255) NOT NULL, -- The manufacturer of the car (e.g., Toyota).
    model VARCHAR(255) NOT NULL, -- The specific model of the car (e.g., Corolla).
    year INT NOT NULL, -- Note: 'year' is a reserved word; Hibernate uses [year] to escape it.
    -- SUGGESTION: Depending on regional standards, license_plate could have a more restrictive length (e.g., VARCHAR(20)) and a specific format constraint.
    license_plate VARCHAR(255) NOT NULL UNIQUE, -- Unique identifier for the car's registration plate.
    color VARCHAR(255), -- The color of the car.
    user_id BIGINT NOT NULL, -- Reference to the owner of the car in the 'users' table.
    -- Relationship: Each car belongs to one user. If a user is deleted, their cars are also removed.
    CONSTRAINT FK_cars_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Optional: Preloaded Data
-- Initial setup with a default administrative user and some sample vehicles.

-- Create a default 'admin' user.
-- Password is 'password123' (BCrypt encoded).
INSERT INTO users (username, password) 
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.TVuHOn2');

-- Preload sample cars for the 'admin' user.
-- NOT NEEDED: Hardcoding '1' for user_id assumes this is the first record inserted. 
-- SUGGESTION: Use a subquery or a variable to get the actual ID of the 'admin' user to make the script more robust.
-- Get the ID of the user we just created (assuming it's 1)
INSERT INTO cars (brand, model, year, license_plate, color, user_id)
VALUES ('Toyota', 'Corolla', 2022, 'ABC-123', 'Silver', 1),
       ('Mazda', 'CX-5', 2023, 'XYZ-789', 'Red', 1);
