CREATE TABLE t_orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(255),
    sku_code VARCHAR(255),
    price NUMERIC(19, 2),
    quantity INTEGER
);