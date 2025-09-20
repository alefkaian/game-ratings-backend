CREATE TABLE esrb_ratings (
    id INT PRIMARY KEY,
    slug VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO esrb_ratings (id, slug, name) VALUES
(1, 'everyone', 'Everyone'),
(2, 'everyone-10-plus', 'Everyone 10+'),
(3, 'teen', 'Teen'),
(4, 'mature', 'Mature'),
(5, 'adults-only', 'Adults Only'),
(6, 'rating-pending', 'Rating Pending');