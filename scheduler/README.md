# scheduler

A scheduling module that creates Camel Quartz routes. 

Use Spring Data/JPA for database access.

# points of interest

The routes are created dynamically from entries in the database. 

# demo mode

I added this as an afterthought, as the Hazelcast module has it, and it would enable running the whole suite without any external (i.e. 'real') database. In keeping with the idea of using many different techniques within the suite, the demo data is loaded using Spring JDBC, by way of schema-demo.sql and data-demo.sql. For this to work, Hibernate's create mechanism is disabled (see application-demo.yml).

To run in demo mode, specify '-Dspring.profiles.active=demo' 

command line: mvn spring-boot:run -Dspring.profiles.active=demo