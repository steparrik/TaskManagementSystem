alter table users alter column password drop not null;

alter table users add column register_type varchar(50) not null;