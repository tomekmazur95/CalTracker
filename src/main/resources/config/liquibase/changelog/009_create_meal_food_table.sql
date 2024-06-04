--liquibase formatted sql
--changeset Thomas:add_meal_food_table
--comment Initial changeset for meal_food_table
create table meal_food
(
    food_quantity double null,
    food_id       bigint null,
    id            bigint auto_increment
        primary key,
    meal_id       bigint null,
    constraint FK1nvfxbtnse9jngkelfbmwefjh
        foreign key (food_id) references foods (id),
    constraint FKcdyqd9s6m83dyixmrlv2usq5n
        foreign key (meal_id) references meals (id)
)

