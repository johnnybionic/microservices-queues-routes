-- used for the 'demo' mode, where an in-memory database is created
DROP TABLE IF EXISTS queue;
DROP TABLE IF EXISTS map_long_to_string;

SET MODE MySQL;

CREATE TABLE queue (
  queue_name varchar(256) NOT NULL,
  queue_key BIGINT NOT NULL,
  item varchar(256) DEFAULT NULL
); 

CREATE TABLE map_long_to_string (
  map_key BIGINT PRIMARY KEY,
  map_value varchar(256) NOT NULL
); 
