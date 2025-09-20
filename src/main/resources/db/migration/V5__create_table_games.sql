CREATE TABLE games (
    id INT PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    tba BOOLEAN,
    release_date DATE,
    background_image VARCHAR(255),
    rating REAL,
    top_rating REAL,
    metacritic INT,
    refresh_time TIMESTAMP,
    publisher_id INT,
    esrb_rating_id INT,
    FOREIGN KEY (publisher_id) REFERENCES publishers(id),
    FOREIGN KEY (esrb_rating_id) REFERENCES esrb_ratings(id)
);