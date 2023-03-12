create table asset
(
    id                  bigint identity not null,
    crest_number        nvarchar(max),
    model               nvarchar(max),
    serial_number       nvarchar(max),
    type                nvarchar(max),
    warranty_expiration datetime2,
    primary key (id)
);

create table change_history
(
    id        bigint identity not null,
    remark    nvarchar(max),
    status    nvarchar(max),
    timestamp datetime2,
    author_id bigint,
    order_id  bigint,
    primary key (id)
);

create table "order"
(
    id              bigint identity not null,
    assignment_date datetime2,
    delivery_type   nvarchar(max),
    pickup_date     datetime2,
    remark          nvarchar(max),
    status          nvarchar(max),
    asset_id        bigint,
    assigned_by_id  bigint,
    assigned_to_id  bigint,
    primary key (id)
);

create table report
(
    id              bigint identity not null,
    from_date       datetime2,
    link            nvarchar(max),
    timestamp       datetime2,
    to_date         datetime2,
    generated_by_id bigint,
    primary key (id)
);

create table role
(
    id   bigint identity not null,
    name nvarchar(max),
    primary key (id)
);

create table "user"
(
    id       bigint identity not null,
    username nvarchar(max),
    email    nvarchar(max),
    password nvarchar(max),
    role_id  bigint,
    primary key (id)
);

alter table change_history
    add constraint change_history_user foreign key (author_id) references "user";
alter table change_history
    add constraint change_history_order foreign key (order_id) references "order";
alter table "order"
    add constraint order_asset foreign key (asset_id) references asset;
alter table "order"
    add constraint assigned_by foreign key (assigned_by_id) references "user";
alter table "order"
    add constraint assigned_to foreign key (assigned_to_id) references "user";
alter table report
    add constraint generated_by foreign key (generated_by_id) references "user";
alter table "user"
    add constraint user_role foreign key (role_id) references role;