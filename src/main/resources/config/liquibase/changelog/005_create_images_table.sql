--liquibase formatted sql
--changeset Thomas:add_images_table
--comment Initial changeset for images_table
create table images
(
    id        bigint auto_increment
        primary key,
    user_id   bigint null,
    name      varchar(255) null,
    type      varchar(255) null,
    imagedata mediumblob null,
    constraint UK_gn0kkmw9cx9tbd2bwc6xxbqr7
        unique (user_id),
    constraint FK13ljqfrfwbyvnsdhihwta8cpr
        foreign key (user_id) references users (id)
)
