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

DELETE FROM `user_info`;
INSERT INTO `user_info` (id, `username`, `password`, `email`, `is_enable`, `create_time`, `modify_time`)
VALUES (1, 'root_test', '123456', 'example@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'});

DELETE FROM database_info;
INSERT INTO database_info (id, host, port, catalog, type, username, password, description, operator, create_time, modify_time)
VALUES ( 1, '127.0.0.1', 3306, 'test_db', 0, 'root', '123456', 'database_info_test_description', 0, {ts '2012-12-31 00:00:00.00'}, {ts '2012-12-31 00:00:00.00'});

DELETE FROM datasource_info;
INSERT INTO datasource_info (id, database_id, schema, username, password, extend_parameters, description, operator, create_time, modify_time)
VALUES ( 1, 1, 'test_source_ds', 'root', '123456', 'characterEncoding=UTF-8,serverTimezone=UTC', 'test_datasource_info_description', 0, {ts '2012-12-31 00:00:00.00'}, {ts '2012-12-31 00:00:00.00'});

DELETE FROM task_execution_log;
INSERT INTO task_execution_log (id, plan_batch, task_batch, task_identifier, execution_step, execution_state, remaining_row, create_timestamp, end_timestamp)
VALUES (1, 1653140066101L, 1653140066107L, 'test_plan#task_aaa', 'N', 'S', 30, 1653140066104L, 1653140066104L),
       (2, 1653140066101L, 1653140066108L, 'test_plan#task_bbb', 'N', 'S', 20, 1653140066104L, 1653140066104L);
