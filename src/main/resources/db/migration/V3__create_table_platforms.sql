CREATE TABLE platforms (
    id INT PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    background_image VARCHAR(255)
);