# Monix Task, Play Framework, Postgres

The Postgres database can be created and pre-loaded with some data by running this simple command (provided you have docker running and docker-compose installed).

```bash
docker-compose up &
```

to check the data is loaded correctly, install potgres tools:

```bash
brew install postgresql
psql -h localhost -p 5432 -U cars
<password is 'cars'>
\dt
#this will show the tables
select * from cars;
#check the data is loaded correctly, stop scrolling through resultset by pressing 'q', and quit psql by pressing Ctrl+D
```


You run the app by typing

```bash
sbt run
```


date started working on this thing: 2018-05-26
