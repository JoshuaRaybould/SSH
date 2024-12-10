CREATE TABLE tenants (
    tenant_id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    tenant_name varchar(255) NOT NULL,
    PRIMARY KEY(tenant_id)
);

CREATE TABLE fridge_items (
    fridge_item_id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    fridge_item_name varchar(255) NOT NULL,
    fridge_item_price money NOT NULL,
    estimated_shelf_life integer NOT NULL,
    food_type varchar(255) NOT NULL,
    PRIMARY KEY(fridge_item_id)
);

CREATE TABLE tenants_fridge_items (
    tenant_id integer NOT NULL references tenants(tenant_id),
    fridge_item_id integer NOT NULL references fridge_items(fridge_item_id),
    quantity integer NOT NULL,
    date_time DATE NOT NULL,
    quality decimal NOT NULL,
    PRIMARY KEY (tenant_id, fridge_item_id)
);

-- INSERT INTO tenants (tenant_id, tenant_name) VALUES (1, 'Jamal');

COPY fridge_items
FROM '/docker-entrypoint-initdb.d/ingredients.csv'
DELIMITER ','
CSV HEADER;

COPY tenants
FROM '/docker-entrypoint-initdb.d/tenants.csv'
DELIMITER ','
CSV HEADER;

COPY tenants_fridge_items
FROM '/docker-entrypoint-initdb.d/tenants_fridge_items.csv'
DELIMITER ','
CSV HEADER;

