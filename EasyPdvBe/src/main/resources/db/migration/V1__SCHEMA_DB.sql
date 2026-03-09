CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(100)       NOT NULL,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash TEXT               NOT NULL,
    role          VARCHAR(20) CHECK (role IN ('ADMIN', 'SALESPERSON', 'MANAGER')),
    is_active     BOOLEAN   DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    cpf             VARCHAR(30)  NOT NULL UNIQUE,
    phone           VARCHAR(20)  NOT NULL,
    credit_limit    DECIMAL(10, 2) DEFAULT 0.00,
    current_balance DECIMAL(10, 2) DEFAULT 0.00,
    created_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE suppliers
(
    id           SERIAL PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL,
    tax_id       VARCHAR(20) UNIQUE,
    contact_info VARCHAR(100)
);

CREATE TABLE categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE products
(
    id          SERIAL PRIMARY KEY,
    barcode     VARCHAR(50) UNIQUE,
    name        VARCHAR(100)   NOT NULL,
    sale_price  DECIMAL(10, 2) NOT NULL,
    category_id INT REFERENCES categories (id),
    active      BOOLEAN DEFAULT TRUE
);

CREATE TABLE batches
(
    id               SERIAL PRIMARY KEY,
    product_id       INT REFERENCES products (id),
    batch_number     VARCHAR(50)    NOT NULL,
    expiry_date      DATE           NOT NULL,
    entry_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    initial_quantity DECIMAL(10, 2) NOT NULL,
    current_quantity DECIMAL(10, 2) NOT NULL, -- Actual stock for this specific batch
    unit_cost        DECIMAL(10, 2) NOT NULL,
    supplier_id      INT REFERENCES suppliers (id)
);

CREATE TABLE purchases
(
    id             SERIAL PRIMARY KEY,
    supplier_id    INT REFERENCES suppliers (id),
    user_id        INT REFERENCES users (id),
    invoice_number VARCHAR(50),
    entry_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount   DECIMAL(10, 2) NOT NULL
);

CREATE TABLE purchase_items
(
    id           SERIAL PRIMARY KEY,
    purchase_id  INT REFERENCES purchases (id) ON DELETE CASCADE,
    product_id   INT REFERENCES products (id),
    batch_number VARCHAR(50),
    expiry_date  DATE,
    quantity     DECIMAL(10, 2) NOT NULL,
    unit_cost    DECIMAL(10, 2) NOT NULL,
    subtotal     DECIMAL(10, 2) NOT NULL
);

CREATE TABLE sales
(
    id           SERIAL PRIMARY KEY,
    customer_id  INT REFERENCES customers (id),
    user_id      INT REFERENCES users (id) NOT NULL,
    sale_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2)            NOT NULL,
    payment_type VARCHAR(20) CHECK (payment_type IN ('CASH', 'DEBIT', 'CREDIT', 'PIX', 'ON_ACCOUNT')),
    is_paid      BOOLEAN   DEFAULT FALSE,
    notes        TEXT
);

CREATE TABLE sale_items
(
    id         SERIAL PRIMARY KEY,
    sale_id    INT REFERENCES sales (id) ON DELETE CASCADE,
    product_id INT REFERENCES products (id),
    batch_id   INT REFERENCES batches (id),
    quantity   DECIMAL(10, 2) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal   DECIMAL(10, 2) NOT NULL
);

CREATE TABLE customer_payments
(
    id             SERIAL PRIMARY KEY,
    customer_id    INT REFERENCES customers (id) NOT NULL,
    user_id        INT REFERENCES users (id),
    amount_paid    DECIMAL(10, 2)                NOT NULL,
    payment_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(20)
);

CREATE TABLE cash_categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(10) CHECK (type IN ('REVENUE', 'EXPENSE'))
);

CREATE TABLE cash_flow
(
    id               SERIAL PRIMARY KEY,
    category_id      INT REFERENCES cash_categories (id),
    user_id          INT REFERENCES users (id),
    description      VARCHAR(200),
    amount           DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    sale_id          INT REFERENCES sales (id),
    payment_method   VARCHAR(20)
);

-- ==========================================================
-- AUTOMATIC TRIGGERS
-- ==========================================================

CREATE
OR REPLACE FUNCTION trg_create_batch_on_purchase() RETURNS TRIGGER AS $$
BEGIN
INSERT INTO batches (product_id, batch_number, expiry_date, initial_quantity, current_quantity, unit_cost, supplier_id)
SELECT NEW.product_id,
       NEW.batch_number,
       NEW.expiry_date,
       NEW.quantity,
       NEW.quantity,
       NEW.unit_cost,
       p.supplier_id
FROM purchases p
WHERE p.id = NEW.purchase_id;
RETURN NEW;
END; $$
LANGUAGE plpgsql;

CREATE TRIGGER t_purchase_to_batch
    AFTER INSERT
    ON purchase_items
    FOR EACH ROW EXECUTE FUNCTION trg_create_batch_on_purchase();


CREATE
OR REPLACE FUNCTION trg_deduct_batch_on_sale() RETURNS TRIGGER AS $$
BEGIN
UPDATE batches
SET current_quantity = current_quantity - NEW.quantity
WHERE id = NEW.batch_id;


IF
(
SELECT current_quantity
FROM batches
WHERE id = NEW.batch_id) < 0 THEN
        RAISE EXCEPTION 'Insufficient stock in this batch.';
END IF;

RETURN NEW;
END; $$
LANGUAGE plpgsql;

CREATE TRIGGER t_sale_from_batch
    AFTER INSERT
    ON sale_items
    FOR EACH ROW EXECUTE FUNCTION trg_deduct_batch_on_sale();