drop table if exists task;
drop table if exists task_history;
drop table if exists task_audit;
create table task (id bigint not null, name varchar(255), source_queue varchar(255), cron_expression varchar(255), delay_period bigint, maximum_attempts bigint, current_attempt bigint, correlation_id varchar(255), state varchar(255), primary key (id));
create table task_history (task_id bigint not null, history varchar(255));
create table task_audit (id bigint not null, task_id bigint, correlation_id varchar(255), message varchar(255), audit_timestamp timestamp, primary key (id));

