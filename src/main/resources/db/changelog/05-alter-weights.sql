ALTER TABLE ingredients ADD COLUMN grams DOUBLE PRECISION;
ALTER TABLE ingredients DROP COLUMN quantity;
ALTER TABLE ingredients DROP COLUMN unit;

ALTER TABLE shopping_items ADD COLUMN grams DOUBLE PRECISION;
ALTER TABLE shopping_items DROP COLUMN quantity;
ALTER TABLE shopping_items DROP COLUMN unit;

ALTER TABLE inventory ADD COLUMN grams DOUBLE PRECISION;
ALTER TABLE inventory DROP COLUMN quantity;
ALTER TABLE inventory DROP COLUMN unit;
