CREATE TABLE meals (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    meal_type VARCHAR(32) NOT NULL,
    description TEXT,
    health_benefits TEXT,
    cooking_time INTEGER NOT NULL,
    is_leftover BOOLEAN NOT NULL,
    recipe TEXT,
    cook_date DATE NOT NULL
);

CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity VARCHAR(255),
    unit VARCHAR(64),
    meal_id BIGINT NOT NULL,
    CONSTRAINT fk_meal FOREIGN KEY (meal_id) REFERENCES meals(id)
);

