# external system simulation

This module simulates an external document service. Clients make requests and receive responses via message queues.
A ReST interface is also available. 

Uses native JDBC for data access.

# points of interest

The module can use either ActiveMQ (default Spring profile) or ApolloMQ ("apollo" Spring profile). There
are some small differences, for example the queue naming is slightly different (see .properties files), plus
the configuration (see *MQConfiguration.java)

