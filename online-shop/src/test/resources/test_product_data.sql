INSERT INTO ref_product_category(
	category_code, category_name, category_description)
	VALUES ('BK', 'Book', 'Book Category');
INSERT INTO ref_product_category(
	category_code, category_name, category_description)
	VALUES ('FD', 'Food', 'Food Category');


INSERT INTO product(
	product_id, category_code, product_name, price, product_description, stock)
	VALUES (1, 'BK', 'Fountainhead', 10, 'A novel', 100);
INSERT INTO product(
	product_id, category_code, product_name, price, product_description, stock)
	VALUES (2, 'BK', 'Core Java', 1, 'Learning', 100);
INSERT INTO product(
	product_id, category_code, product_name, price, product_description, stock)
	VALUES (3, 'FD', 'Pizza', 20, 'Fast food 1', 100);