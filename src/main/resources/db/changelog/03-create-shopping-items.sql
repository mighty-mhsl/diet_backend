CREATE TABLE shopping_items (
    id SERIAL PRIMARY KEY,
    ingredient_name VARCHAR(255) NOT NULL,
    quantity VARCHAR(255),
    unit VARCHAR(64),
    is_purchased BOOLEAN NOT NULL,
    estimated_cost VARCHAR(64),
    plan_date DATE NOT NULL
);
