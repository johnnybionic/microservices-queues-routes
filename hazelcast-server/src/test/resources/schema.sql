DROP TABLE IF EXISTS queue;
DROP TABLE IF EXISTS map_string_to_string;

SET MODE MySQL;

CREATE TABLE queue (
  queue_name varchar(256) NOT NULL,
  queue_key BIGINT NOT NULL,
  item varchar(256) DEFAULT NULL
); 

-- no longer used
--CREATE TABLE map_long_to_string (
--  map_key BIGINT PRIMARY KEY,
--  map_value varchar(256) NOT NULL
--); 

CREATE TABLE map_string_to_string (
  map_key varchar(256) PRIMARY KEY,
  map_value varchar(256) NOT NULL
); 
