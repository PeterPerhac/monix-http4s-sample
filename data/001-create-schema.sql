CREATE TABLE cars (
  id serial primary key,
  year smallint not null,
  make text not null,
  model text not null,
  unique (year, make, model)
);

