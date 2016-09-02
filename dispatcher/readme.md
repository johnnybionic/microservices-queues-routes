# dispatcher

This module sends entries on the Hazelcast queues to the external server. It must ensure that only one request from a particular queue is processed at a time, and that other requests wait until the head of the queue has successfully processed. Using queues enables this 'in-order' processing. 

The module creates scheduled tasks (using Spring/Java Scheduling) from entries in the database. When run, these tasks 

- checks if there is already a request of this type processing; if so the task exits
- polls the relevant queue; if the queue is empty, the task exits
- takes the head off the queue, and creates an external request from it
- assigns a unique correlation ID for the request
- places the request on the external queue, and records an entry in the database (which is what's checked in the first step)

At this point, the task is complete - it's 'fire and forget', so does not await a response from the other system. That will eventually arrive via the response queue, and the process here is:

- save the response somewhere (a simple file will suffice for an example)
- use the correlation ID to find the entry in the database
- mark the entry as complete, which allows other requests from its associated queue to be processed (i.e. step one of the process above)

# in-memory database

The 'demo' profile starts the app with an in-memory H2 database. This is useful as it creates the database from the domain classes - in particular to see how the Task class @ElementCollection is implemented*. The H2 console is available at

http://localhost:8030/h2-console

using jdbc:h2:mem:dispatcher_store as the URL, 'sa' as the user and no password.

The database is initialised using Hibernate features - see application-demo.yml 

*I have had a few 'discussions' with DBAs as to whether it's a good idea to let Hibernate/JPA create the database from the application's model, or whether to create the model from the database :)