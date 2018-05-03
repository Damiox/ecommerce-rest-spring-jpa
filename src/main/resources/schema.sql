create table if not exists app_user (
    id bigint,
    username varchar(50) not null,
    password varchar(100) not null,
    role varchar(10) not null,
    primary key (id),
    unique key username (username)
);

create table if not exists app_category (
    id bigint,
    name varchar(100) not null,
    parentid bigint,
    primary key (id),
    foreign key (parentid) references app_category(id)
);

create table if not exists app_product (
    id bigint,
    name varchar(300) not null,
    price double not null,
    userid bigint not null,
    primary key (id),
    foreign key (userid) references app_user(id)
);

create table if not exists app_product_category (
    productid bigint,
    categoryid bigint,
    primary key (productid, categoryid),
    foreign key (productid) references app_product(id),
    foreign key (categoryid) references app_category(id)
);

create sequence if not exists hibernate_sequence start with 100;
