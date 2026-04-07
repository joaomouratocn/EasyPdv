CREATE TYPE MOVEMENT_TYPE AS ENUM ('ENTRY', 'EXIT', 'PAYMENT');
CREATE TYPE PAYMENT_METHOD AS ENUM ('MO', 'PI', 'DC', 'CC', 'VO', 'SP');
CREATE TYPE SALE_STATUS AS ENUM ('OPEN', 'CLOSE', 'CANCELED', 'SPUN');
CREATE TYPE USER_ROLE AS ENUM ('ADMIN', 'USER');
CREATE TYPE PRICE_TYPE AS ENUM ('BUY', 'SALE');

CREATE TABLE "customer" (
    "id" UUID PRIMARY KEY,
    "name" varchar(50) NOT NULL,
    "cpf" varchar(15) UNIQUE NOT NULL,
    "phone" varchar(20) NOT NULL,
    "limit" decimal(13,2) DEFAULT 600
);

CREATE TABLE "pos" (
    "id" UUID PRIMARY KEY,
    "name" varchar(15)
);

CREATE TABLE "pos_flow" (
    "id" UUID NOT NULL,
    "user_id" UUID NOT NULL,
    "pos_id" UUID NOT NULL,
    "init_balance" decimal(13,2) NOT NULL,
    "end_balance" decimal(13,2),
    "init_date" timestamp,
    "end_date" timestamp,
    "open" boolean,
    PRIMARY KEY ("id", "user_id")
);

CREATE TABLE "pos_movement" (
    "id" UUID PRIMARY KEY,
    "pos_id" UUID NOT NULL,
    "user_id" UUID NOT NULL,
    "value" decimal(13,2) NOT NULL,
    "type" MOVEMENT_TYPE NOT NULL,
    "description" varchar(100),
    "created_at" timestamp DEFAULT (now()),
    "sale_id" UUID
);

CREATE TABLE "sale" (
    "id" UUID PRIMARY KEY,
    "user_id" UUID NOT NULL,
    "pos_id" UUID NOT NULL,
    "customer_id" UUID,
    "gross_value" decimal(13,2),
    "discount_value" decimal(13,2),
    "final_value" decimal(13,2),
    "payment_method" PAYMENT_METHOD NOT NULL,
    "status" SALE_STATUS,
    "created_at" timestamp NOT NULL DEFAULT (now()),
    "ended_at" timestamp
);

CREATE TABLE "item_sale" (
     "id" UUID PRIMARY KEY,
     "sale_id" UUID NOT NULL,
     "product_id" UUID NOT NULL,
     "amount" decimal(13,2) NOT NULL,
     "unit_price_origin" decimal(13,2) NOT NULL,
     "discount_percent" decimal(13,2),
     "discount_value" decimal(13,2),
     "final_unit_price" decimal(13,2) NOT NULL
);

CREATE TABLE "category" (
    "id" UUID PRIMARY KEY,
    "name" varchar(50) UNIQUE NOT NULL,
    "active" boolean NOT NULL DEFAULT true,
    "created_at" timestamp DEFAULT (now())
);

CREATE TABLE "measure" (
    "id" UUID PRIMARY KEY,
    "name" varchar(50) UNIQUE NOT NULL,
    "active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "products" (
    "id" UUID PRIMARY KEY,
    "name" varchar(50) NOT NULL,
    "description" varchar(100),
    "barcode" varchar(15) UNIQUE NOT NULL,
    "markup" decimal(13,2) NOT NULL,
    "category_id" UUID NOT NULL,
    "measure_id" UUID NOT NULL,
    "stock" decimal(13,2),
    "buy_price" decimal(13,2) NOT NULL,
    "sale_price" decimal(13,2) NOT NULL,
    "min_stock" decimal(13,2) DEFAULT 1,
    "alert_stock" boolean DEFAULT true,
    "active" boolean DEFAULT true,
    "created_at" timestamp DEFAULT (now())
);

CREATE TABLE "history_price" (
     "id" UUID PRIMARY KEY,
     "product_id" UUID NOT NULL,
     "type_price" PRICE_TYPE NOT NULL,
     "old_price" decimal(13,2) NOT NULL,
     "new_price" decimal(13,2) NOT NULL,
     "alter_date" timestamp DEFAULT (now()),
     "user_id" UUID NOT NULL
);

CREATE TABLE "users" (
     "id" UUID PRIMARY KEY,
     "name" varchar(50) NOT NULL,
     "username" varchar(50) UNIQUE NOT NULL,
     "born_date" timestamp NOT NULL,
     "cpf" varchar(15) UNIQUE NOT NULL,
     "role" USER_ROLE NOT NULL,
     "active" boolean NOT NULL
);

CREATE TABLE "stock_movement" (
    "id" UUID PRIMARY KEY,
    "product_id" UUID NOT NULL,
    "type" MOVEMENT_TYPE NOT NULL,
    "amount" decimal(13,2) NOT NULL,
    "date" timestamp DEFAULT (now()),
    "origin" varchar(30),
    "user_id" UUID NOT NULL
);

ALTER TABLE "pos_flow" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
ALTER TABLE "pos_flow" ADD FOREIGN KEY ("pos_id") REFERENCES "pos" ("id");
ALTER TABLE "pos_movement" ADD FOREIGN KEY ("pos_id") REFERENCES "pos" ("id");
ALTER TABLE "pos_movement" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
ALTER TABLE "pos_movement" ADD FOREIGN KEY ("sale_id") REFERENCES "sale" ("id");
ALTER TABLE "sale" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
ALTER TABLE "sale" ADD FOREIGN KEY ("pos_id") REFERENCES "pos" ("id");
ALTER TABLE "sale" ADD FOREIGN KEY ("customer_id") REFERENCES "customer" ("id");
ALTER TABLE "item_sale" ADD FOREIGN KEY ("sale_id") REFERENCES "sale" ("id");
ALTER TABLE "products" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id");
ALTER TABLE "products" ADD FOREIGN KEY ("measure_id") REFERENCES "measure" ("id");
ALTER TABLE "history_price" ADD FOREIGN KEY ("product_id") REFERENCES "products" ("id");
ALTER TABLE "history_price" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
ALTER TABLE "stock_movement" ADD FOREIGN KEY ("product_id") REFERENCES "products" ("id");
ALTER TABLE "stock_movement" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
