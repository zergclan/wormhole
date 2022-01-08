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

DELETE FROM database_info;
INSERT INTO database_info (id, host, port, catalog, type, username, password, description, operator, create_time, modify_time)
VALUES ( 1, '127.0.0.1', 3306, 'source_db', 0, 'root', '123456', 'MySQL测试源数据库', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
        ( 2, '127.0.0.1', 3307, 'target_db', 0, 'root', '123456', 'MySQL测试源数据库', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

DELETE FROM datasource_info;
INSERT INTO datasource_info (id, database_id, schema, username, password, extend_parameters, description, operator, create_time, modify_time)
VALUES ( 1, 1, 'source_db', 'root', '123456', 'characterEncoding=UTF-8&serverTimezone=UTC', 'MySQL测试源数据源', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
    ( 1, 2, 'target_db', 'root', '123456', 'characterEncoding=UTF-8&serverTimezone=UTC', 'MySQL测试目前数据源', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

DELETE FROM plan_info;
INSERT INTO plan_info (id, title, code, execution_mode, execution_corn, execution_count, effective_date, is_enable, is_execute, description, operator, create_time, modify_time)
VALUES ( 1, 'MySQL测试方案', 'mysql_test_plan', '0', '0 0 12 * * ?', 0, '2012-12-01 10:30:30.00', 0, 0, 'MySQL测试方案', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

DELETE FROM task_info;
INSERT INTO task_info (id, title, code, source_id, target_id, is_enable, is_execute, description, operator, create_time, modify_time)
VALUES ( 1, 'MySQL测试任务一对一', 'mysql_test_task_one_to_one', 1, 2, 0, 0, 'MySQL测试任务一对一', 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

DELETE FROM plan_task_linking;
INSERT INTO plan_task_linking (id, plan_id, task_id, order, operator, create_time, modify_time)
VALUES ( 1, 1, 1, 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});

