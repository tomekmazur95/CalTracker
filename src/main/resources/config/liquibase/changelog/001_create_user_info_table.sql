--liquibase formatted sql
--changeset Thomas:add_user_info_table
--comment Initial changeset for userinfo_table
create table users_info
(
    id       bigint auto_increment
        primary key,
    email    varchar(255) null,
    password varchar(255) null,
    role     enum ('ADMIN', 'MANAGER', 'USER') null
)