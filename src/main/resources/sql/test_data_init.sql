insert into shop (name, balance) values ('Sergey Shop', 20394.47);

insert into candy (name, price) VALUES ('Candy 1', 100.23);
insert into candy (name, price) VALUES ('Candy 2', 283.95);
insert into candy (name, price) VALUES ('Candy 3', 493.54);
insert into candy (name, price) VALUES ('Candy 4', 109.49);
insert into candy (name, price) VALUES ('Candy 5', 495.29);

insert into candy_fabric (name, components) values ('Danila Candy Fabric', 3);
insert into supplier (name) values ('supplier');

insert into users (username, password, active) values ('shop', '$2a$10$gLwGw3/06DXsaliseRiGie/PJqYmye9zqOIFMEvHtNO2RojozpURW', true);
insert into users (username, password, active) values ('fabric', '$2a$10$hpVLzstg6ZbMaz8A9P.afuEN4lUL4ONVAeJlHVI40oLn0NycPOafG', true);
insert into users (username, password, active) values ('supplier', '$2a$10$hsG8wYNC0KrwvWdIIq1qq.H1IZ1ojV2uhre05JnI0Br3MbvrOJpaW', true);
insert into users (username, password, active) values ('admin', '$2a$10$6AWqFB37BIYKdv77XY1CfO3xsJLXa5wUTayCzGfQQX70ap6fZuIkm', true);

insert into user_roles (user_id, roles) values (1, 'SHOP_USER');
insert into user_roles (user_id, roles) values (2, 'FABRIC_USER');
insert into user_roles (user_id, roles) values (3, 'SUPPLIER_USER');
insert into user_roles (user_id, roles) values (4, 'ADMIN');