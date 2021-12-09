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

DELETE FROM `datasource_info`;

INSERT INTO `datasource_info` (`id`, `title`, `datasource_type`, `host`, `port`, `catalog`, `username`, `password`, `extend_parameters`, `url`, `description`, `is_enable`, `operator`, `create_time`, `modify_time`)
VALUES ( 1, 'datasource_info_test1', 0, '127.0.0.1', 3306, 'ds_test1', 'root_test', '123456',
        '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
        'jdbc:mysql://127.0.0.1:3306/ds_test1?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
        'datasource info for ut test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
        ( 2, 'datasource_info_test2', 0, '127.0.0.1', 3306, 'ds_test2', 'root_test', '123456',
        '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
        'jdbc:mysql://127.0.0.1:3306/ds_test2?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
        'datasource info for ut test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});
