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

DROP DATABASE IF EXISTS expected_source;
DROP DATABASE IF EXISTS expected_target;

CREATE DATABASE expected_source;
CREATE DATABASE expected_target;

CREATE TABLE expected_source.t_user (id int NOT NULL AUTO_INCREMENT, username varchar(64) NOT NULL, password varchar(64) NOT NULL, PRIMARY KEY (id));
CREATE TABLE expected_target.t_user (id int NOT NULL AUTO_INCREMENT, username varchar(64) NOT NULL, password varchar(64) NOT NULL, PRIMARY KEY (id));

