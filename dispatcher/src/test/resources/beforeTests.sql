insert into task (id, name, delay_period) values (1L, 'Task one', 5000);
insert into task (id, name, cron_expression, state) values (2L, 'Task two', '0/5***', 'IDLE');
insert into task (id, name, cron_expression, state) values (3L, 'Task three', '0/5***', 'RUNNING');
insert into task (id, name, cron_expression, state, last_accessed) values (4L, 'Task four', '0/5***', 'RUNNING', {ts '2016-08-31 15:06:52.69'});

insert into task_history values (1L, 'Started');

insert into task_audit (id, task_id, correlation_id, message, audit_timestamp) values (1L, 1L, '123', 'Sent', parsedatetime('17-09-2012 18:47:52.69', 'dd-MM-yyyy hh:mm:ss.SS'))
--insert into task_audit (id, task_id, correlation_id, message) values (1L, 1L, '123', , 'Sent')