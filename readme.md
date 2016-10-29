
# microservices-queues-routes

[![Build status](https://travis-ci.org/johnnybionic/microservices-queues-routes.svg?branch=master)](https://travis-ci.org/johnnybionic/microservices-queues-routes)
[![Coverage Status](https://coveralls.io/repos/github/johnnybionic/microservices-queues-routes/badge.svg?branch=master)](https://coveralls.io/github/johnnybionic/microservices-queues-routes?branch=master)

A collection of Spring Boot microservices that show how services can be decoupled and interact 
asynchronously using Hazelcast queues, ActiveMQ/Apollo queues, Camel and REST.

Overview
--------

The basic function of the suite is to request documents from an external system. 
- The 'external' module periodically fires requests for documents, which are placed onto a Hazelcast queue. There can be any number of these queues
- The 'dispatcher' module reads the Hazelcast queues, and sends a request to the external system, using ActiveMQ or Apollo. Each new request has a correlation ID to allow the response to be matched to the request. A request cannot be sent if a previous request of the same type is pending, and if a request times out or reports an error, no more requests of that type can be sent. Responses from the external system are saved to disk.
- The 'external' module simulates an external system. It receives requests via queues or REST, and returns the requested document. 

Aside from all using Spring Boot, I've tried to use different technologies in each module. For example, the modules use different ways to connect to the database.   

![Alt text](architecture.jpg?raw=true "Architecture")

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

# dispatcher

Port: 8030

Polls the Hazelcast queues and sends any entries to the queue listened to by the external service.
The dispatcher must only send one entry from a particular Hazel queue at a time, and must await
the response. 
Provides a ReST interface to test the queues, and also to the minimal dispatcher database. 

---
 