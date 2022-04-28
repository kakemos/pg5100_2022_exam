create table users(
    user_id bigserial primary key,
    user_email varchar(50),
    user_password varchar(100),
    user_created timestamp,
    user_enabled bool
);

create table authorities(
    authority_id bigserial primary key,
    authority_name varchar(50)
);

create table users_authorities(
    user_id bigint,
    authority_id bigint
);

create table animals(
    animal_id bigserial primary key,
    animal_name varchar(50),
    animal_type varchar(50),
    animal_breed varchar(50),
    animal_gender integer,
    animal_age integer,
    animal_adopted bool,
    animal_created timestamp,
    animal_enabled bool,
    user_id bigint
);