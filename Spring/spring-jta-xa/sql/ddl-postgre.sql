create sequence product_id_seq start with 1 increment by 1;

create table TBL_PRODUCT (
    id INTEGER not null,
    name VARCHAR(200),
    price double precision,
    count INTEGER default 0,
    primary key (id)
);

insert into TBL_PRODUCT values (1, 'BMW X5', 100000, 5);
insert into TBL_PRODUCT values (2, 'BMW 362', 90000, 1);

create sequence account_id_seq start with 1 increment by 1;

create table TBL_ACCOUNT (
    id INTEGER not null,
    name VARCHAR(200),
    product_name VARCHAR(200),
    count INTEGER default 0,
    price double precision,
    total double precision,
    primary key (id)
);

create sequence order_id_seq start with 1 increment by 1;

create table TBL_ORDER (
    id INTEGER not null,
    name VARCHAR(200),
    product_name VARCHAR(200),
    price double precision,
    primary key (id)
);