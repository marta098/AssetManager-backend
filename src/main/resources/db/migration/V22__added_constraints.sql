-- global_variabloe
alter table global_variable
    alter column value varchar(255) not null
go

-- location
alter table location
    alter column name varchar(255) not null
go

create unique index location_name_uindex
    on location (name)
go

-- mpk_number
alter table mpk_number
    alter column mpk nvarchar(max) not null
go


-- order
UPDATE [order]
SET order_number = '2023-07-19-1'
WHERE id = 1;

UPDATE [order]
SET receiver_id = 1
WHERE id = 1;

UPDATE [order]
SET added_date = '2022-01-25 13:20:00.0000000'
WHERE id = 1;

UPDATE [order]
SET mpk_number_id = 1
WHERE id = 1;

UPDATE [order]
SET requested_model = 'SMALL_LAPTOP'
WHERE id = 1;

UPDATE [order]
SET requester_id = 1
WHERE id = 1;


UPDATE [order]
SET order_number = '2023-07-19-2'
WHERE id = 2;

UPDATE [order]
SET receiver_id = 2
WHERE id = 2;

UPDATE [order]
SET added_date = '2022-01-30 13:20:00.0000000'
WHERE id = 2;

UPDATE [order]
SET mpk_number_id = 2
WHERE id = 2;

UPDATE [order]
SET requested_model = 'SMALL_LAPTOP'
WHERE id = 2;

UPDATE [order]
SET requester_id = 2
WHERE id = 2;


alter table [order]
    alter column delivery_type nvarchar(max) not null
go

alter table [order]
    alter column status nvarchar(max) not null
go

alter table [order]
    alter column order_number nvarchar(max) not null
go

alter table [order]
    alter column receiver_id bigint not null
go

alter table [order]
    alter column added_date datetime2 not null
go

alter table [order]
    alter column requested_model varchar(255) not null
go

alter table [order]
    alter column requester_id bigint not null
go

alter table [order]
    alter column is_dhl_reminder_sent bit not null
go

alter table [order]
    alter column is_it_reminder_sent bit not null
go


-- report
alter table report
    alter column timestamp datetime2 not null
go

alter table report
    alter column to_date datetime2 not null
go

alter table report
    alter column generated_by_id bigint not null
go

-- role
alter table role
    alter column name nvarchar(max) not null
go


-- stockroom
alter table stockroom
    alter column name varchar(255) not null
go

create unique index stockroom_name_uindex
    on stockroom (name)
go

-- user
alter table [user]
    alter column username nvarchar(max) not null
go

alter table [user]
    alter column email nvarchar(max) not null
go

alter table [user]
    alter column password nvarchar(max) not null
go

alter table [user]
    alter column role_id bigint not null
go

alter table [user]
    alter column is_deleted bit not null
go


-- asset
UPDATE [asset]
SET deprecation = '2025-07-19 19:14:27.000'
WHERE id = 1;

UPDATE [asset]
SET current_user_id = 1
WHERE id = 1;

UPDATE [asset]
SET crest_code = 1
WHERE id = 1;

UPDATE [asset]
SET status = 'IN_USE'
WHERE id = 1;

UPDATE [asset]
SET location_id = 1
WHERE id = 1;

UPDATE [asset]
SET model = 'SMALL_LAPTOP'
WHERE id = 1;


UPDATE [asset]
SET deprecation = '2025-07-19 19:14:27.000'
WHERE id = 2;

UPDATE [asset]
SET current_user_id = 2
WHERE id = 2;

UPDATE [asset]
SET crest_code = 1
WHERE id = 2;

UPDATE [asset]
SET status = 'IN_USE'
WHERE id = 2;

UPDATE [asset]
SET location_id = 2
WHERE id = 2;

UPDATE [asset]
SET model = 'SMALL_LAPTOP'
WHERE id = 2;


UPDATE [asset]
SET deprecation = '2025-07-19 19:14:27.000'
WHERE id = 3;

UPDATE [asset]
SET current_user_id = 3
WHERE id = 3;

UPDATE [asset]
SET crest_code = 3
WHERE id = 3;

UPDATE [asset]
SET status = 'IN_USE'
WHERE id = 3;

UPDATE [asset]
SET location_id = 3
WHERE id = 3;

UPDATE [asset]
SET model = 'SMALL_LAPTOP'
WHERE id = 3;

alter table asset
    alter column serial_number nvarchar(max) not null
go

alter table asset
    alter column type nvarchar(max) not null
go

alter table asset
    alter column deprecation datetime not null
go

alter table asset
    alter column status nvarchar(max) not null
go

alter table asset
    alter column model varchar(255) not null
go

-- asset_change_history
alter table asset_change_history
    alter column timestamp datetime not null
go

alter table asset_change_history
    alter column status nvarchar(255) not null
go

-- change_history
alter table change_history
    alter column status nvarchar(max) not null
go

alter table change_history
    alter column timestamp datetime2 not null
go

alter table change_history
    alter column author_id bigint not null
go

alter table change_history
    alter column order_id bigint not null
go
