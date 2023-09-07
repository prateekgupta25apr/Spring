-- version 1
alter table table_1 add column col_2 tinyint(1);
alter table table_1 add column col_1 varchar(500);
create table if not exists table_1 (primary_key int auto_increment primary key);

create schema sample_project_1;