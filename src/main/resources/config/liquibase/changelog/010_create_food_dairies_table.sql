--liquibase formatted sql
--changeset Thomas:add_food_dairies_table
--comment Initial changeset for food_dairies_table
create table food_dairies
(
    date          date         null,
    food_quantity int          null,
    meal_quantity int          null,
    meal_type     tinyint      null,
    food_id       bigint       null,
    id            bigint auto_increment
        primary key,
    meal_id       bigint       null,
    user_id       bigint       null,
    name          varchar(255) null,
    constraint FK2jstp2wspmbt04u6a10a9t8jm
        foreign key (user_id) references users (id),
    constraint FKq0d3co7uecx3y9so0p40rkkvs
        foreign key (meal_id) references meals (id),
    constraint FKqmxdpwidr1wcsyr6slhsbrgsa
        foreign key (food_id) references foods (id)
)

