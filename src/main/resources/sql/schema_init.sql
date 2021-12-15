create table candy (
    id bigserial not null,
    name varchar(255),
    price double precision,
    primary key (id)
);

create table candy_fabric (
    id bigserial not null,
    name varchar(255),
    components int8,
    primary key (id)
);

create table orders (
    id bigserial not null,
    price double precision,
    in_progress bool,
    delivered bool,
    shop_order bool,
    fabric_order bool,
    candy_id bigserial,
    primary key (id)
);

create table supplier (
    id bigserial not null,
    name varchar(255),
    primary key (id)
);

create table shop (
    id bigserial not null,
    name varchar(255),
    balance double precision,
    primary key (id)
);

create table users (
    id bigserial not null,
    username varchar(255),
    password varchar(255),
    active bool,
    primary key (id)
);

create table roles (
    user_id bigserial not null,
    role_name varchar(255)
);

create table shop_orders (
    shop_id bigserial not null,
    orders_id bigserial not null
);

create table candy_fabric_orders (
    candy_fabric_id bigserial not null,
    orders_id bigserial not null
);

create table supplier_orders (
    supplier_id bigserial not null,
    orders_id bigserial not null
);


alter table if exists orders add constraint fk_orders_candy foreign key (candy_id) references candy;
alter table if exists shop_orders add constraint fk_shop_orders_shop foreign key (shop_id) references shop;
alter table if exists shop_orders add constraint fk_shop_orders_orders foreign key (orders_id) references orders;
alter table if exists candy_fabric_orders add constraint fk_candy_fabric_orders_candy_fabric foreign key (candy_fabric_id) references candy_fabric;
alter table if exists candy_fabric_orders add constraint fk_candy_fabric_orders_orders foreign key (orders_id) references orders;
alter table if exists supplier_orders add constraint fk_supplier_orders_supplier foreign key (supplier_id) references supplier;
alter table if exists supplier_orders add constraint fk_supplier_orders_orders foreign key (orders_id) references orders;