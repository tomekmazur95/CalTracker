--liquibase formatted sql
--changeset Thomas:add_users_table
--comment Initial changeset for users_table
create table users
(
    age          int null,
    id           bigint auto_increment
        primary key,
    user_info_id bigint null,
    activity     enum ('EXTRA_ACTIVE', 'LIGHTLY_ACTIVE', 'MODERATELY_ACTIVE', 'SEDENTARY', 'VERY_ACTIVE') null,
    gender       enum ('FEMALE', 'MALE') null,
    user_name    varchar(255) null,
    constraint UK_65t6bc8nlb8lpnk86aimnl7pd
        unique (user_info_id),
    constraint FKa4pav0806byoi689xgfobjqic
        foreign key (user_info_id) references users_info (id)
)