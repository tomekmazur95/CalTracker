--liquibase formatted sql
--changeset Thomas:add_foods_table
--comment Initial changeset for foods_table
create table foods
(
    date         date         null,
    food_fact_id bigint       null,
    id           bigint auto_increment
        primary key,
    user_id      bigint       null,
    description  varchar(255) null,
    name         varchar(255) null,
    constraint UK_a2klme2rdbn0r7fct8v9oycfr
        unique (food_fact_id),
    constraint FK7nt3msq7jumk2llmvrnsqm6x5
        foreign key (user_id) references users (id),
    constraint FKbawbpgfpckn7m9xi4ocqq7u46
        foreign key (food_fact_id) references food_facts (id)
)

