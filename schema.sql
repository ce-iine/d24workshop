drop database if exists d24wksp;

create database d24wksp;

use d24wksp;

create table orders (
   order_id int auto_increment,
   order_date date,
   customer_name varchar(128) not null,
   ship_address varchar(128) not null,
   notes varchar(128),
   tax decimal(6, 2) default 0.05,

   primary key(order_id)
);

create table order_details (
   id int auto_increment primary key, 
   product varchar(64) not null,
   unit_price decimal(6, 2),
   discount decimal(6, 2) default 1.0,
   quantity int,
   order_id int,

   constraint fk_order_id foreign key(order_id) references orders(order_id)
);

grant all privileges on d24wksp.* to celine@'%';

flush privileges;