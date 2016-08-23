insert into scheduled_task (id, name, cron) values (1, 'st1', '*/5 * * * *');
insert into scheduled_task (id, name, cron, comments) values (2, 'st2', '*/1 * * * *', 'Task two');
insert into scheduled_task (id, name, cron) values (3, 'st3', '*884--');

insert into task_request (id, name, document, queue, scheduled_task_id) values (1, 'tr1', '{id:1}', 'queue.1', 1);
insert into task_request (id, name, document, queue, scheduled_task_id) values (2, 'tr2', '{id:2}', 'queue.1', 1);
insert into task_request (id, name, document, queue, scheduled_task_id) values (3, 'tr3', '{id:3}', 'queue.1', 1);

insert into task_request (id, name, document, queue, scheduled_task_id) values (11, 'tr11', '{id:11}', 'queue.2', 2);
insert into task_request (id, name, document, queue, scheduled_task_id) values (12, 'tr12', '{id:12}', 'queue.2', 2);
insert into task_request (id, name, document, queue, scheduled_task_id) values (13, 'tr13', '{id:13}', 'queue.2', 2);

