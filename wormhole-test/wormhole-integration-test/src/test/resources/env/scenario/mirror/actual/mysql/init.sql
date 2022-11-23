--
-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- create user for database
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
GRANT All privileges ON *.* TO 'root'@'%';

DROP DATABASE IF EXISTS actual_source;
DROP DATABASE IF EXISTS actual_target;

CREATE DATABASE actual_source;
CREATE DATABASE actual_target;

CREATE TABLE actual_source.t_user (id int NOT NULL AUTO_INCREMENT, username varchar(64) NOT NULL, password varchar(64) NOT NULL, create_time datetime NOT NULL, PRIMARY KEY (id));
CREATE TABLE actual_target.t_user (id int NOT NULL AUTO_INCREMENT, task_batch bigint NOT NULL, username varchar(64) NOT NULL, password varchar(64) NOT NULL, `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (id));

INSERT INTO actual_source.t_user (id, username, password, create_time)
VALUES (1, 'jack1', '123456', '2022-01-01 08:10:10'), (2, 'jack2', '123456', '2022-01-01 08:10:10'),
       (3, 'jack3', '123456', '2022-01-01 08:10:10'), (4, 'jack4', '123456', '2022-01-01 08:10:10'),
       (5, 'jack5', '123456', '2022-01-01 08:10:10'), (6, 'jack6', '123456', '2022-01-01 08:10:10'),
       (7, 'jack7', '123456', '2022-01-01 08:10:10'), (8, 'jack8', '123456', '2022-01-01 08:10:10'),
       (9, 'jack9', '123456', '2022-01-01 08:10:10');
