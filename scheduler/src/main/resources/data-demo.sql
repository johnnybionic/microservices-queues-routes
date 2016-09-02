INSERT INTO scheduled_task (id,name,cron,comments) VALUES (1,'task 1','cron=0+0/5+*+?+*+*','Every five minutes');
INSERT INTO scheduled_task (id,name,cron,comments) VALUES (2,'task 2','cron=0+0/1+*+?+*+*','Every minute');

INSERT INTO task_request (id,name,document,queue,scheduled_task_id) VALUES (1,'tr1','{id:1}','queue.1',1);
INSERT INTO task_request (id,name,document,queue,scheduled_task_id) VALUES (2,'tr2','{id:2}','queue.2',2);
