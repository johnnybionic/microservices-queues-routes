# hazelcast-spring-microservices

A collection of Spring Boot microservices that show how services can be decoupled and interact 
asynchronously using Hazelcast queues, ActiveMQ queues and ReST.

Aside from all using Spring Boot, I've tried to use different technologies in each module. For example, each one
connects to its database in a different way.   

The project contains these modules (refer to each for more details). Each has a default port, overridable with '--port'

# hazelcast-server

Port: 8000
A server providing centralised Hazelcast maps and queues, with persistence.
Provides a ReST interface to the Hazelcast store. 

# external

Port: 8010
This represents an external system, from which documents are requested. It is meant to simulate
delays and failures that can be expected when working with external resources. Listens to message queues 
(e.g. ActiveMQ).  
Provides a ReST interface to the document database.

# scheduler 

Port: 8020
Creates Camel CRON jobs that fire requests for documents, and place them onto Hazelcast queues.
Provides a ReST interface to the task database.
 