create sequence product_id_seq start with 1 increment by 1 nomaxvalue;

create table TBL_PRODUCT (
    id INTEGER  not null,
    name VARCHAR2(200),
    price NUMBER,
    count INTEGER default 0,
    constraint PK_TBL_PRODUCT primary key (id)
);

insert into TBL_PRODUCT values (1, 'BMW X5', 100000, 5);
insert into TBL_PRODUCT values (2, 'BMW 362', 90000, 1);

create sequence account_id_seq start with 1 increment by 1 nomaxvalue;

create table TBL_ACCOUNT (
    id INTEGER not null,
    name VARCHAR2(200),
    product_name VARCHAR2(200),
    count INTEGER default 0,
    price NUMBER,
    total NUMBER,
    constraint PK_TBL_ACCOUNT primary key (id)
);

create sequence order_id_seq start with 1 increment by 1 nomaxvalue;

create table TBL_ORDER (
    id INTEGER not null,
    name VARCHAR2(200),
    product_name VARCHAR2(200),
    price NUMBER,
    constraint PK_TBL_ORDER primary key (id)
);