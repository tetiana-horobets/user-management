create table user (
    user_pk int(11) not null auto_increment,
    user_name varchar(100) not null,
    password varchar(100) not null,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    date_of_birth date not null,
    primary key (user_pk)
);

create table agroup (
    group_pk int(11) not null auto_increment,
    group_name varchar(100) not null,
    primary key (group_pk)
);

create table user_group (
    user_id int(11) not null,
    group_id int(11) not null,
    primary key (user_id, group_id),
    constraint fk_user foreign key (user_id) references user (user_pk),
    constraint fk_group foreign key (group_id) references agroup (group_pk)
);