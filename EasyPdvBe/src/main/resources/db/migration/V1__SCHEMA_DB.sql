-- ==========================================================
-- 1. BASE TABLES
-- ==========================================================

CREATE TABLE users (
                       id            SERIAL PRIMARY KEY,
                       name          VARCHAR(100)       NOT NULL,
                       username      VARCHAR(50) UNIQUE NOT NULL,
                       password_hash TEXT               NOT NULL,
                       role          VARCHAR(20) CHECK (role IN ('ADMIN', 'SALESPERSON', 'MANAGER')),
                       is_active     BOOLEAN   DEFAULT TRUE,
                       created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
                           id              SERIAL PRIMARY KEY,
                           name            VARCHAR(100) NOT NULL,
                           tax_id          VARCHAR(30)  NOT NULL UNIQUE, -- CPF/CNPJ
                           phone           VARCHAR(20)  NOT NULL,
                           credit_limit    DECIMAL(10, 2) DEFAULT 0.00,
                           current_balance DECIMAL(10, 2) DEFAULT 0.00,
                           created_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE suppliers (
                           id           SERIAL PRIMARY KEY,
                           company_name VARCHAR(150) NOT NULL,
                           tax_id       VARCHAR(20) UNIQUE,
                           contact_info VARCHAR(100)
);

CREATE TABLE categories (
                            id   SERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL UNIQUE
);

-- ==========================================================
-- 2. PRODUCT & BARCODE STRUCTURE (One-to-Many)
-- ==========================================================

CREATE TABLE products (
                          id          SERIAL PRIMARY KEY,
                          name        VARCHAR(100)   NOT NULL,
                          sale_price  DECIMAL(10, 2) NOT NULL,
                          category_id INT REFERENCES categories (id),
                          is_active   BOOLEAN DEFAULT TRUE
);

CREATE TABLE product_barcodes (
                                  id         SERIAL PRIMARY KEY,
                                  product_id INT REFERENCES products (id) ON DELETE CASCADE,
                                  barcode    VARCHAR(50) UNIQUE NOT NULL,
                                  is_main    BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_barcode_lookup ON product_barcodes(barcode);

-- ==========================================================
-- 3. INVENTORY & TRANSACTIONS
-- ==========================================================

CREATE TABLE batches (
                         id               SERIAL PRIMARY KEY,
                         product_id       INT REFERENCES products (id),
                         batch_number     VARCHAR(50)    NOT NULL,
                         expiry_date      DATE           NOT NULL,
                         entry_date       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         initial_quantity DECIMAL(10, 2) NOT NULL,
                         current_quantity DECIMAL(10, 2) NOT NULL,
                         unit_cost        DECIMAL(10, 2) NOT NULL,
                         supplier_id      INT REFERENCES suppliers (id)
);

CREATE TABLE sales (
                       id           SERIAL PRIMARY KEY,
                       customer_id  INT REFERENCES customers (id),
                       user_id      INT REFERENCES users (id) NOT NULL,
                       sale_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       total_amount DECIMAL(10, 2)            NOT NULL,
                       payment_type VARCHAR(20) CHECK (payment_type IN ('CASH', 'DEBIT', 'CREDIT', 'PIX', 'ON_ACCOUNT')),
                       is_paid      BOOLEAN   DEFAULT FALSE,
                       notes        TEXT
);

CREATE TABLE sale_items (
                            id         SERIAL PRIMARY KEY,
                            sale_id    INT REFERENCES sales (id) ON DELETE CASCADE,
                            product_id INT REFERENCES products (id),
                            batch_id   INT REFERENCES batches (id),
                            quantity   DECIMAL(10, 2) NOT NULL,
                            unit_price DECIMAL(10, 2) NOT NULL,
                            subtotal   DECIMAL(10, 2) NOT NULL
);

-- ==========================================================
-- 4. VIEWS & FUNCTIONS
-- ==========================================================

-- View to see total stock by any valid barcode
CREATE OR REPLACE VIEW view_product_stock AS
SELECT
    p.id AS product_id,
    pb.barcode,
    p.name AS product_name,
    c.name AS category_name,
    COALESCE(SUM(b.current_quantity), 0) AS total_stock,
    MIN(b.expiry_date) AS next_expiry,
    p.sale_price
FROM products p
         JOIN product_barcodes pb ON p.id = pb.product_id
         LEFT JOIN categories c ON p.category_id = c.id
         LEFT JOIN batches b ON p.id = b.product_id AND b.current_quantity > 0
WHERE p.is_active = TRUE
GROUP BY p.id, pb.barcode, p.name, c.name, p.sale_price;

-- Quick stock check function
CREATE OR REPLACE FUNCTION fn_check_stock_availability(p_product_id INT, p_qty DECIMAL)
RETURNS BOOLEAN AS $$
BEGIN
RETURN (SELECT COALESCE(SUM(current_quantity), 0) FROM batches WHERE product_id = p_product_id) >= p_qty;
END;
$$ LANGUAGE plpgsql;

-- ==========================================================
-- 5. PROCEDURES & TRIGGERS (The Logic)
-- ==========================================================

-- Trigger to deduct stock from batch when sale_item is inserted
CREATE OR REPLACE FUNCTION fn_trg_deduct_stock() RETURNS TRIGGER AS $$
BEGIN
UPDATE batches SET current_quantity = current_quantity - NEW.quantity WHERE id = NEW.batch_id;
IF (SELECT current_quantity FROM batches WHERE id = NEW.batch_id) < 0 THEN
        RAISE EXCEPTION 'Insufficient stock in Batch ID %', NEW.batch_id;
END IF;
RETURN NEW;
END; $$ LANGUAGE plpgsql;

CREATE TRIGGER trg_after_sale_item_insert
    AFTER INSERT ON sale_items FOR EACH ROW EXECUTE FUNCTION fn_trg_deduct_stock();

-- Core Procedure: Sells using FIFO (Expiration Date) and handles batch splitting
CREATE OR REPLACE PROCEDURE pr_process_sale_item(
    p_sale_id INT,
    p_product_id INT,
    p_required_qty DECIMAL
)
LANGUAGE plpgsql
AS $$
DECLARE
r_batch RECORD;
    v_remaining_qty DECIMAL := p_required_qty;
    v_qty_to_deduct DECIMAL;
    v_price DECIMAL;
BEGIN
SELECT sale_price INTO v_price FROM products WHERE id = p_product_id;

-- FIFO: Sort by expiry_date first, then entry_date
FOR r_batch IN
SELECT id, current_quantity
FROM batches
WHERE product_id = p_product_id AND current_quantity > 0
ORDER BY expiry_date ASC, entry_date ASC
    LOOP
        EXIT WHEN v_remaining_qty <= 0;

v_qty_to_deduct := LEAST(r_batch.current_quantity, v_remaining_qty);

INSERT INTO sale_items (sale_id, product_id, batch_id, quantity, unit_price, subtotal)
VALUES (p_sale_id, p_product_id, r_batch.id, v_qty_to_deduct, v_price, (v_qty_to_deduct * v_price));

v_remaining_qty := v_remaining_qty - v_qty_to_deduct;
END LOOP;

    IF v_remaining_qty > 0 THEN
        RAISE EXCEPTION 'Not enough total stock for Product ID %. Deficit: %', p_product_id, v_remaining_qty;
END IF;
END;
$$;