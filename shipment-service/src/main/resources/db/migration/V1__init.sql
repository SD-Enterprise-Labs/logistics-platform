create table shipments (
    id          uuid primary key,
    origin      varchar(120) not null,
    destination varchar(120) not null,
    weight_kg   double precision not null,
    status      varchar(30) not null,
    created_at  timestamptz not null
);

create table outbox_event (
    id           uuid primary key,
    aggregate_id varchar(64) not null,
    topic        varchar(120) not null,
    payload      text not null,
    created_at   timestamptz not null,
    published_at timestamptz
);

create index idx_outbox_unpublished on outbox_event (created_at) where published_at is null;
