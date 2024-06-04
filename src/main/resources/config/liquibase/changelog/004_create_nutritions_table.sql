--liquibase formatted sql
--changeset Thomas:add_nutritions_table
--comment Initial changeset for nutritions_table
create table nutritions
(
    carbohydrate_percent double                                                                                        null,
    date                 date                                                                                          null,
    fat_percent          double                                                                                        null,
    protein_percent      double                                                                                        null,
    carbohydrate         bigint                                                                                        null,
    fat                  bigint                                                                                        null,
    id                   bigint auto_increment
        primary key,
    measurement_id       bigint                                                                                        null,
    protein              bigint                                                                                        null,
    unit                 enum ('CALORIES', 'CENTIMETERS', 'GRAMS', 'KILOGRAMS', 'KILOMETERS', 'METERS', 'MILLIMETERS') null,
    constraint FK2t7lofjidny9burqgeqwexdg6
        foreign key (measurement_id) references measurements (id)
)