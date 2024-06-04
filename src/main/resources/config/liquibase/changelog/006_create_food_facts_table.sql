--liquibase formatted sql
--changeset Thomas:add_food_facts_table
--comment Initial changeset for food_facts_table
create table food_facts
(
    calories     double                                                                                        null,
    carbohydrate double                                                                                        null,
    date         date                                                                                          null,
    fat          double                                                                                        null,
    protein      double                                                                                        null,
    value        double                                                                                        null,
    id           bigint auto_increment
        primary key,
    unit         enum ('CALORIES', 'CENTIMETERS', 'GRAMS', 'KILOGRAMS', 'KILOMETERS', 'METERS', 'MILLIMETERS') null
)
