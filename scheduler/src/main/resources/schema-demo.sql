CREATE TABLE scheduled_task (
  id INTEGER PRIMARY KEY,
  name varchar(45) DEFAULT NULL,
  cron varchar(45) DEFAULT NULL,
  comments varchar(100) DEFAULT NULL
);

CREATE TABLE task_request (
  id INTEGER PRIMARY KEY,
  name varchar(45) DEFAULT NULL,
  document varchar(2048) DEFAULT NULL,
  queue varchar(45) DEFAULT NULL,
  scheduled_task_id INTEGER,
) ;


