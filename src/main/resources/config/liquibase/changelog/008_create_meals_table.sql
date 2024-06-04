--liquibase formatted sql
--changeset Thomas:add_meals_table
--comment Initial changeset for meals_table
create table meals
(
    date        date         null,
    id          bigint auto_increment
        primary key,
    user_id     bigint       null,
    description varchar(255) null,
    name        varchar(255) null,
    constraint FK677c66qpjr7234luomahc1ale
        foreign key (user_id) references users (id)
)

