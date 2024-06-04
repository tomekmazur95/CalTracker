--liquibase formatted sql
--changeset Thomas:add_measurements_table
--comment Initial changeset for measurements_table
create table measurements
(
    date    date                                                                                                                    null,
    value   double                                                                                                                  null,
    id      bigint auto_increment
        primary key,
    user_id bigint                                                                                                                  null,
    type    enum ('CURRENT_WEIGHT', 'ENERGY_DEFICIT', 'ENERGY_SURPLUS', 'ENERGY_TDEE', 'GOAL_WEIGHT', 'HEIGHT', 'LENGTH', 'WEIGHT') null,
    unit    enum ('CALORIES', 'CENTIMETERS', 'GRAMS', 'KILOGRAMS', 'KILOMETERS', 'METERS', 'MILLIMETERS')                           null,
    constraint FKa6kjrhmi7c4w4y0djie5yifpg
        foreign key (user_id) references users (id)
)