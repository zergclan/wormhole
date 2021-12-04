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

INSERT INTO `user_info` (id, `username`, `password`, `email`, `enable`, `create_time`, `modify_time`) VALUES
(1, 'jack', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(2, 'rose', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(3, 'root', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(4, 'admin', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(5, 'java', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(6, 'guava', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(7, 'gson', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(8, 'lombok', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(9, 'lang3', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(10, 'validation', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(11, 'disruptor', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'}),
(12, 'h2', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.00'}, {ts '2012-11-19 18:30:30.00'});

DELETE FROM `datasource_info`;

INSERT INTO `datasource_info` (`id`, `title`, `genre`, `host`, `port`, `catalog`, `username`, `password`, `extend_parameters`, `url`, `description`, `enable`, `operator`, `create_time`, `modify_time`)
VALUES ( 1, 'local_extracted_data_source', 0, '127.0.0.1', 3306, 'extracted_ds', 'root', '123456',
        '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
        'jdbc:mysql://127.0.0.1:3306/extracted_ds?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
        'extracted datasource for test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
       ( 2, 'local_loading_data_source', 0, '127.0.0.1', 3306, 'loading_ds', 'root', '123456',
         '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
         'jdbc:mysql://127.0.0.1:3306/loading_ds?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
         'loading datasource for test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
       ( 3, 'local_test_data_source1', 0, '127.0.0.1', 3307, 'ds_ut', 'root', '123456',
         '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
         'jdbc:mysql://127.0.0.1:3306/ds1?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
         'datasource info for ut test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
       ( 4, 'local_test_data_source2', 0, '127.0.0.1', 3307, 'ds2', 'root', '123456',
         '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
         'jdbc:mysql://127.0.0.1:3306/ds2?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
         'datasource info for test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
       ( 5, 'local_test_data_source3', 0, '127.0.0.1', 3307, 'ds3', 'root', '123456',
         '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
         'jdbc:mysql://127.0.0.1:3306/ds3?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
         'datasource info for test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
       ( 6, 'local_test_data_source4', 0, '127.0.0.1', 3307, 'ds4', 'root', '123456',
         '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
         'jdbc:mysql://127.0.0.1:3306/ds4?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
         'datasource info for test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'}),
       ( 7, 'local_test_data_source5', 0, '127.0.0.1', 3307, 'ds5', 'root', '123456',
         '{"useSSL":"false","useUnicode":"true","characterEncoding":"UTF-8","serverTimezone":"UTC"}',
         'jdbc:mysql://127.0.0.1:3306/ds5?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC',
         'datasource info for test', 0, 0, {ts '2012-12-01 10:30:30.00'}, {ts '2012-12-01 10:30:30.00'});
