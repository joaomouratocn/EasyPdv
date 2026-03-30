CREATE TYPE MOVEMENT_TYPE AS ENUM ('ENTRY', 'EXIT', 'PAYMENT');
CREATE TYPE PAYMENT_METHOD AS ENUM ('MO', 'PI', 'DC', 'CC', 'VO', 'SP');
CREATE TYPE SALE_STATUS AS ENUM ('OPEN', 'CLOSE', 'CANCELED', 'SPUN');
CREATE TYPE USER_ROLE AS ENUM ('ADMIN', 'USER');
CREATE TYPE PRICE_TYPE AS ENUM ('BUY', 'SALE');

CREATE TABLE "customer" (
  "id" BIGSERIAL PRIMARY KEY,
  "name" varchar(50) NOT NULL,
  "cpf" varchar(15) UNIQUE NOT NULL,
  "phone" varchar(20) NOT NULL,
  "limit" decimal(13,2) DEFAULT 600
);

CREATE TABLE "pos" (
  "id" BIGSERIAL PRIMARY KEY,
  "name" varchar(15)
);

CREATE TABLE "pos_flow" (
  "id" BIGSERIAL,
  "user_id" bigint NOT NULL,
  "pos_id" bigint NOT NULL,
  "init_balance" decimal(13,2) NOT NULL,
  "end_balance" decimal(13,2),
  "init_date" timestamp,
  "end_date" timestamp,
  "open" boolean,
  PRIMARY KEY ("id", "user_id")
);

CREATE TABLE "pos_movement" (
  "id" BIGSERIAL PRIMARY KEY,
  "pos_id" bigint NOT NULL,
  "user_id" bigint NOT NULL,
  "value" decimal(13,2) NOT NULL,
  "type" MOVEMENT_TYPE NOT NULL,
  "description" varchar(100),
  "created_at" timestamp DEFAULT 'now()',
  "sale_id" integer
);

CREATE TABLE "sale" (
  "id" BIGSERIAL PRIMARY KEY,
  "user_id" bigint NOT NULL,
  "pos_id" bigint NOT NULL,
  "customer_id" integer,
  "gross_value" decimal(13,2),
  "discount_value" decimal(13,2),
  "final_value" decimal(13,2),
  "payment_method" PAYMENT_METHOD NOT NULL,
  "status" SALE_STATUS,
  "created_at" timestamp NOT NULL DEFAULT 'now()',
  "ended_at" timestamp
);

CREATE TABLE "item_sale" (
  "id" BIGSERIAL PRIMARY KEY,
  "sale_id" bigint NOT NULL,
  "product_id" bigint NOT NULL,
  "amount" decimal(13,2) NOT NULL,
  "unit_price_origin" decimal(13,2) NOT NULL,
  "discount_percent" decimal(13,2),
  "discount_value" decimal(13,2),
  "final_unit_price" decimal(13,2) NOT NULL
);

CREATE TABLE "category" (
  "id" BIGSERIAL PRIMARY KEY,
  "name" varchar(50) UNIQUE NOT NULL,
  "active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "products" (
  "id" BIGSERIAL PRIMARY KEY,
  "name" varchar(50) NOT NULL,
  "description" varchar(100),
  "barcode" varchar(15) UNIQUE NOT NULL,
  "measure" varchar(5) NOT NULL,
  "balance" decimal(13,2),
  "buy_price" decimal(13,2) NOT NULL,
  "sale_price" decimal(13,2) NOT NULL,
  "category_id" bigserial NOT NULL,
  "min_amount" decimal(13,2) DEFAULT 1,
  "alert_enable" boolean DEFAULT true,
  "created_at" timestamp DEFAULT (now())
);

CREATE TABLE "history_price" (
  "id" BIGSERIAL PRIMARY KEY,
  "product_id" bigint NOT NULL,
  "type_price" PRICE_TYPE NOT NULL,
  "old_price" decimal(13,2) NOT NULL,
  "new_price" decimal(13,2) NOT NULL,
  "alter_date" timestamp DEFAULT 'now()',
  "user_id" integer NOT NULL
);

CREATE TABLE "users" (
  "id" BIGSERIAL PRIMARY KEY,
  "name" varchar(50) NOT NULL,
  "username" varchar(50) UNIQUE NOT NULL,
  "born_date" timestamp NOT NULL,
  "cpf" varchar(15) UNIQUE NOT NULL,
  "role" USER_ROLE NOT NULL,
  "active" boolean NOT NULL
);

CREATE TABLE "stock_movement" (
  "id" BIGSERIAL PRIMARY KEY,
  "product_id" bigint NOT NULL,
  "type" MOVEMENT_TYPE NOT NULL,
  "amount" decimal(13,2) NOT NULL,
  "date" timestamp DEFAULT 'now()',
  "origin" varchar(30),
  "user_id" integer NOT NULL
);

ALTER TABLE "pos_flow" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pos_flow" ADD FOREIGN KEY ("pos_id") REFERENCES "pos" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pos_movement" ADD FOREIGN KEY ("pos_id") REFERENCES "pos" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pos_movement" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "pos_movement" ADD FOREIGN KEY ("sale_id") REFERENCES "sale" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale" ADD FOREIGN KEY ("pos_id") REFERENCES "pos" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale" ADD FOREIGN KEY ("customer_id") REFERENCES "customer" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "item_sale" ADD FOREIGN KEY ("sale_id") REFERENCES "sale" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "products" ADD FOREIGN KEY ("category_id") REFERENCES "category" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "history_price" ADD FOREIGN KEY ("product_id") REFERENCES "products" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "history_price" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock_movement" ADD FOREIGN KEY ("product_id") REFERENCES "products" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock_movement" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY IMMEDIATE;
