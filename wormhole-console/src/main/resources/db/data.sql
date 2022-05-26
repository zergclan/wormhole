/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

DELETE FROM user_info;
INSERT INTO user_info (id, username, password, email, is_enable, create_time, modify_time) VALUES
(1, 'root', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'});

DELETE FROM error_data_log;
INSERT INTO error_data_log (id, task_identifier, plan_batch, task_batch, code, message, owner_identifier, data_json, create_timestamp)
VALUES (1, 'test_task_1', 1653140066101L, 1653140066102L, 'NOT_NULL', '不能为空', 'mysql#127.0.0.1@3307', '{"name","test_name"}', 1653140066104L),
    (2, 'test_task_1', 1653140066101L, 1653140066102L, 'NOT_NULL', '不能为空', 'mysql#127.0.0.1@3307', '{"name","test_name"}', 1653140066104L),
    (3, 'test_task_2', 1653140066105L, 1653140066106L, 'NOT_NULL', '不能为空', 'mysql#127.0.0.1@3307', '{"name","test_name"}', 1653140066104L),
    (4, 'test_task_2', 1653140066105L, 1653140066106L, 'NOT_NULL', '不能为空', 'mysql#127.0.0.1@3307', '{"name","test_name"}', 1653140066104L);

DELETE FROM plan_execution_log;
INSERT INTO plan_execution_log (id, plan_identifier, plan_batch, trigger_identifier, execution_state, create_timestamp, end_timestamp)
VALUES (1, 'test_plan', 1653140066101L, 'ONE_OFF#1653140066102', 'F', 1653140066104L, 1653140066104L),
       (2, 'test_plan', 1653140066102L, 'ONE_OFF#1653140066102', 'F', 1653140066104L, 1653140066104L),
       (3, 'test_plan', 1653140066103L, 'ONE_OFF#1653140066102', 'F', 1653140066104L, 1653140066104L),
       (4, 'test_plan', 1653140066104L, 'ONE_OFF#1653140066102', 'F', 1653140066104L, 1653140066104L);

DELETE FROM task_execution_log;
INSERT INTO task_execution_log (id, plan_batch, task_batch, task_identifier, execution_state, create_timestamp, end_timestamp)
VALUES (1, 1653140066101L, 1653140066107L, 'test_plan#task_aaa', 'S', 1653140066104L, 1653140066104L),
       (2, 1653140066101L, 1653140066108L, 'test_plan#task_bbb', 'S', 1653140066104L, 1653140066104L),
       (3, 1653140066101L, 1653140066109L, 'test_plan#task_ccc', 'F', 1653140066104L, 1653140066104L),
       (4, 1653140066102L, 1653140066114L, 'test_plan#task_ccc', 'S', 1653140066104L, 1653140066104L);

DELETE FROM data_group_execution_log;
INSERT INTO data_group_execution_log (id, task_batch, `batch_index`, total_row, insert_row, update_row, error_row, same_row, create_timestamp, end_timestamp)
VALUES (1, 1653140066107L, 1, 10, 3, 2, 2, 3, 1653140066104L, 1653140066104L),
       (2, 1653140066107L, 2, 10, 3, 2, 2, 3, 1653140066104L, 1653140066104L),
       (3, 1653140066107L, 3, 10, 3, 2, 2, 3, 1653140066104L, 1653140066104L),
       (4, 1653140066107L, 4, 10, 3, 2, 2, 3, 1653140066104L, 1653140066104L);

DELETE FROM database_info;
INSERT INTO database_info (id, host, port, catalog, type, username, password, description, operator, create_time, modify_time)
VALUES ( 1, '127.0.0.1', 3306, 'source_db', 0, 'root', '123456', 'MySQL测试源数据库', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
        ( 2, '127.0.0.1', 3307, 'target_db', 0, 'root', '123456', 'MySQL测试源数据库', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

DELETE FROM datasource_info;
INSERT INTO datasource_info (id, database_id, schema, username, password, extend_parameters, description, operator, create_time, modify_time)
VALUES ( 1, 1, 'source_db', 'root', '123456', 'characterEncoding=UTF-8&serverTimezone=UTC', 'MySQL测试源数据源', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
    ( 2, 2, 'target_db', 'root', '123456', 'characterEncoding=UTF-8&serverTimezone=UTC', 'MySQL测试目前数据源', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

DELETE FROM plan_info;
INSERT INTO plan_info (id, title, code, execution_mode, execution_corn, execution_count, effective_date, is_enable, is_execute, description, operator, create_time, modify_time)
VALUES ( 1, 'MySQL测试方案', 'mysql_test_plan', '0', '0 0 12 * * ?', 0, '2012-12-01 10:30:30.00', 0, 0, 'MySQL测试方案', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

DELETE FROM task_info;
INSERT INTO task_info (id, title, code, source_datasource_id, target_datasource_id, is_enable, is_execute, description, operator, create_time, modify_time)
VALUES ( 1, 'MySQL测试任务一对一', 'mysql_test_task_one_to_one', 1, 2, 0, 0, 'MySQL测试任务一对一', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

DELETE FROM plan_task_linking;
INSERT INTO plan_task_linking (id, plan_id, task_id, `order`, operator, create_time, modify_time)
VALUES ( 1, 1, 1, 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

