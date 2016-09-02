truncate table task_history;
-- truncate not working for task table
delete from task where id > 0;
truncate table task_audit;
