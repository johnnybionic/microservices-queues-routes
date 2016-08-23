# hazelcast-server

Provides centralised, persistent maps and queues.

The purpose of this module is to provide long-lived maps and queues (hours, days, months ...). It demonstrates how to 
add persistence to Hazelcast.

Monitoring of the maps and queues is available via ReST, using the standard controller/service/dao pattern. Example ReST calls
are available for Postman in:
 
src\test\resources\postman_collection.json

Note that the calls are read-only; changes cannot be made via ReST.

Uses Spring JDBC templates for data access.

# points of interest

There is only one store for the maps, but queues require a store per queue ('prototype' bean scope).

'Upsert' is used to save the map entries - MySQL supports this by a non-standard extension, and luckily H2 can
emulate this.


# example client

See GettingStarted.java in src/test/java/com/johnny/nonjunit for an example of how to connect to and use the server.

# demo mode

When deployed, the app uses a MySQL database for persistence. However, it can be started in 'demo' mode, in which 
case an H2 in-memory database is created. To start in this mode, use 'demo' as the Spring profile:

-Dspring.profiles.active=demo 

(as a VM argument).  

The database is not populated, there are scripts in src/test/resources for this, or run the example client.

An H2 console is available at

http://localhost:8080/h2-console/

Use  jdbc:h2:mem:hazelcast_store as the conenction, and 'sa' as the user (no password).

