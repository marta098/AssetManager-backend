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