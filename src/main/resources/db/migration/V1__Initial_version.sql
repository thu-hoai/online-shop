-- CREATE DATABASE onlineshop
--     WITH
--     OWNER = postgres
--     ENCODING = 'UTF8'
--     LC_COLLATE = 'English_United States.1252'
--     LC_CTYPE = 'English_United States.1252'
--     TABLESPACE = pg_default
--     CONNECTION LIMIT = -1;

-- user
CREATE TABLE public.user (
	user_id bigint NOT NULL,
	user_name character varying(50),
    first_name character varying(255),
    last_name character varying(255),
    user_address character varying(255),
    phone character varying(20),
    user_enabled boolean,
	email character varying(255),
	user_password character varying(255)
);

CREATE SEQUENCE public.user_user_id_seq
	START WITH 1
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;

ALTER SEQUENCE public.user_user_id_seq OWNED BY public.user.user_id;
ALTER TABLE ONLY public.user ALTER COLUMN user_id SET DEFAULT nextval('user_user_id_seq'::regclass);
ALTER TABLE ONLY public.user ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);
INSERT INTO public."user"(
	user_name, first_name, last_name, user_address, phone, user_enabled, email, user_password)
	VALUES ('username1', 'firstName1', 'lastName1', 'Ho Chi Minh City', '+84912345667', true, 'username1@gmail.com', '$2a$10$qJvkQYc2PjVwEXsRS0VEq.bedlRlPwssYSi.J/U6tAn77plRGSt.a');
INSERT INTO public."user"(
	user_name, first_name, last_name, user_address, phone, user_enabled, email)
	VALUES ('username2', 'firstName2', 'lastName2', 'Ho Chi Minh City', '+84912345666', true, 'username1@gmail.com');

-- role
CREATE TABLE public.role (
    role_id integer NOT NULL,
    name character varying(255),
    description character varying(512)
);

CREATE SEQUENCE public.role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.role_role_id_seq OWNED BY public.role.role_id;
ALTER TABLE ONLY public.role ALTER COLUMN role_id SET DEFAULT nextval('public.role_role_id_seq'::regclass);
SELECT pg_catalog.setval('public.role_role_id_seq', 1, false);
ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_id_pkey PRIMARY KEY (role_id);

INSERT INTO public.role(name, description)
	VALUES('Administrator', 'Administrator');
INSERT INTO public.role(name, description)
	VALUES('User', 'User');
--
-- Name: User Role; Type: TABLE; user_role
--
CREATE TABLE public.user_role (
    role_id integer,
    user_id integer
);

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user__reference_user FOREIGN KEY (user_id) REFERENCES public.user(user_id) ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fk_user__reference_role FOREIGN KEY (role_id) REFERENCES public.role(role_id) ON UPDATE RESTRICT ON DELETE RESTRICT;

INSERT INTO public.user_role(role_id, user_id)
	VALUES(1, 1);
INSERT INTO public.user_role(role_id, user_id)
	VALUES(2, 2);

-- ref payment
CREATE TABLE public.ref_payment_code (
    payment_code character varying(10),
    payment_description character varying(50)
);

ALTER TABLE ONLY public.ref_payment_code
    ADD CONSTRAINT ref_payment_code_pkey PRIMARY KEY (payment_code);

INSERT INTO public.ref_payment_code(
	payment_code, payment_description)
	VALUES ('CC', 'Credit Card');
INSERT INTO public.ref_payment_code(
	payment_code, payment_description)
	VALUES ('COD', 'Cash On Delivery');

-- user payment
CREATE TABLE public.user_payment (
    user_payment_id int NOT NULL,
    ref_payment_code_payment_code character varying(10),
    user_user_id bigint NOT NULL
);
CREATE SEQUENCE public.user_payment_payment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.user_payment_payment_id_seq OWNED BY public.user_payment.user_payment_id;

ALTER TABLE ONLY public.user_payment ALTER COLUMN user_payment_id SET DEFAULT nextval('public.user_payment_payment_id_seq'::regclass);
SELECT pg_catalog.setval('public.user_payment_payment_id_seq', 1, false);

ALTER TABLE ONLY public.user_payment
    ADD CONSTRAINT user_payment_pkey PRIMARY KEY (user_payment_id);
ALTER TABLE ONLY public.user_payment
    ADD CONSTRAINT fk_user_payment__ref_payment_code FOREIGN KEY (ref_payment_code_payment_code) REFERENCES public.ref_payment_code(payment_code)  ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY public.user_payment
    ADD CONSTRAINT fk_user_payment__user FOREIGN KEY (user_user_id) REFERENCES public.user(user_id)  ON UPDATE RESTRICT ON DELETE RESTRICT;

INSERT INTO public.user_payment(
	ref_payment_code_payment_code, user_user_id)
	VALUES ('COD', 2);

-- product category
CREATE TABLE public.ref_product_category (
    category_code character varying(50),
    category_name character varying(255),
    category_description character varying(512)
);
ALTER TABLE ONLY public.ref_product_category
    ADD CONSTRAINT ref_product_category_pkey PRIMARY KEY (category_code);

INSERT INTO public.ref_product_category(
	category_code, category_name, category_description)
	VALUES ('BK', 'Book', 'Book Category');
INSERT INTO public.ref_product_category(
	category_code, category_name, category_description)
	VALUES ('FD', 'Food', 'Food Category');

-- product
CREATE TABLE public.product (
    product_id bigint NOT NULL,
    category_code character varying(50),
    product_name character varying(255),
    price decimal(10, 2),
    product_description character varying(512)
);
CREATE SEQUENCE public.product_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY public.product ALTER COLUMN product_id SET DEFAULT nextval('public.product_product_id_seq'::regclass);

SELECT pg_catalog.setval('public.product_product_id_seq', 1, false);

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (product_id);
ALTER TABLE ONLY public.product
    ADD CONSTRAINT fk_product__ref_product_category FOREIGN KEY (category_code) REFERENCES public.ref_product_category(category_code)  ON UPDATE RESTRICT ON DELETE RESTRICT;

INSERT INTO public.product(
	category_code, product_name, price, product_description)
	VALUES ('BK', 'Fountainhead', 100.1, 'A novel');
INSERT INTO public.product(
	category_code, product_name, price, product_description)
	VALUES ('BK', 'Core Java', 50, 'Learning');
INSERT INTO public.product(
	category_code, product_name, price, product_description)
	VALUES ('FD', 'Pizza', 20, 'Fast food 1');

-- ref order item status
CREATE TABLE public.ref_order_item_status (
    order_item_status_code character varying(50),
    item_status_description character varying(255)
);
ALTER TABLE ONLY public.ref_order_item_status
    ADD CONSTRAINT ref_order_item_status_pkey PRIMARY KEY (order_item_status_code);

INSERT INTO public.ref_order_item_status(
	order_item_status_code, item_status_description)
	VALUES ('AVL', 'Available');
INSERT INTO public.ref_order_item_status(
	order_item_status_code, item_status_description)
	VALUES ('OOS', 'Out of stock');

-- ref order status
CREATE TABLE public.ref_order_status (
    order_status_code character varying(50),
    order_status_description character varying(255)
);
ALTER TABLE ONLY public.ref_order_status
    ADD CONSTRAINT ref_order_status_code_pkey PRIMARY KEY (order_status_code);

INSERT INTO public.ref_order_status(
	order_status_code, order_status_description)
	VALUES ('NEW', 'New Order');
INSERT INTO public.ref_order_status(
	order_status_code, order_status_description)
	VALUES ('CRM', 'Confirmed Order');
INSERT INTO public.ref_order_status(
	order_status_code, order_status_description)
	VALUES ('CAL', 'Cancel');
INSERT INTO public.ref_order_status(
	order_status_code, order_status_description)
	VALUES ('CML', 'Completed order');

-- order
CREATE TABLE public.order (
    order_id bigint NOT NULL,
    user_id bigint NOT NULL,
    order_status_code character varying(50),
    date_order_placed timestamp without time zone
);
CREATE SEQUENCE public.order_order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY public.order ALTER COLUMN order_id SET DEFAULT nextval('public.order_order_id_seq'::regclass);

SELECT pg_catalog.setval('public.order_order_id_seq', 1, false);

ALTER TABLE ONLY public.order
    ADD CONSTRAINT order_order_id_pkey PRIMARY KEY (order_id);
ALTER TABLE ONLY public.order
    ADD CONSTRAINT fk_order__ref_order_status FOREIGN KEY (order_status_code) REFERENCES public.ref_order_status(order_status_code)  ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY public.order
    ADD CONSTRAINT fk_order__user FOREIGN KEY (user_id) REFERENCES public.user(user_id)  ON UPDATE RESTRICT ON DELETE RESTRICT;

INSERT INTO public."order"(
	user_id, order_status_code, date_order_placed)
	VALUES (2, 'NEW', '2021-11-01');
INSERT INTO public."order"(
	user_id, order_status_code, date_order_placed)
	VALUES (2, 'CML', '2021-10-01');

-- order item
CREATE TABLE public.order_item (
    id bigint NOT NULL,
    product_id bigint NOT NULL,
    order_id bigint NOT NULL,
    order_item_status_code character varying(50),
    quantity int,
    price decimal(10, 2)
);
CREATE SEQUENCE public.order_item_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY public.order_item ALTER COLUMN id SET DEFAULT nextval('public.order_item_id_seq'::regclass);

SELECT pg_catalog.setval('public.order_item_id_seq', 1, false);

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT order_item_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fk_order_item__ref_order_item_status FOREIGN KEY (order_item_status_code) REFERENCES public.ref_order_item_status(order_item_status_code)  ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fk_order_item__product FOREIGN KEY (product_id) REFERENCES public.product(product_id)  ON UPDATE RESTRICT ON DELETE RESTRICT;
ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fk_order_item__order FOREIGN KEY (order_id) REFERENCES public.order(order_id)  ON UPDATE RESTRICT ON DELETE RESTRICT;

INSERT INTO public.order_item(
	product_id, order_id, order_item_status_code, quantity, price)
	VALUES (1, 1, 'AVL', 2, 100.1);
INSERT INTO public.order_item(
	product_id, order_id, order_item_status_code, quantity, price)
	VALUES (3, 1, 'AVL', 4, 20);
INSERT INTO public.order_item(
	product_id, order_id, order_item_status_code, quantity, price)
	VALUES (1, 2, 'AVL', 2, 100.1);
