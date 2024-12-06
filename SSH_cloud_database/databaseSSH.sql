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
    item_type varchar(64)
    PRIMARY KEY(fridge_item_id)
);

CREATE TABLE tenants_fridge_items (
    tenant_id integer NOT NULL references tenants(tenant_id),
    fridge_item_id integer NOT NULL references fridge_items(fridge_item_id),
    quantity integer NOT NULL,
    date_time timestamp NOT NULL,
    quality decimal NOT NULL,
    PRIMARY KEY (tenant_id, fridge_item_id)
);


COPY fridge_items
FROM '/docker-entrypoint-initdb.d/ingredients.csv'
DELIMITER ','
CSV HEADER;

COPY recipes
FROM '/docker-entrypoint-initdb.d/recipes.csv'
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

