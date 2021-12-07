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

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(64) NOT NULL COMMENT '用户密码',
    `email` VARCHAR(64) NOT NULL COMMENT '用户邮箱',
    `is_enable` tinyint(4) NOT NULL COMMENT '0未激活，1激活',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户信息表';


DROP TABLE IF EXISTS `datasource_info`;
CREATE TABLE datasource_info(
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `title` VARCHAR(64) NOT NULL COMMENT '数据源名称',
    `datasource_type` tinyint(4) NOT NULL COMMENT '数据源类型',
    `host` VARCHAR(16) NOT NULL COMMENT '数据源IP',
    `port` INT(11) NOT NULL COMMENT '数据源端口',
    `catalog` VARCHAR(64) NOT NULL COMMENT '数据库名称',
    `username` VARCHAR(64) NOT NULL COMMENT '链接用户名',
    `password` VARCHAR(64) NOT NULL COMMENT '链接用户密码',
    `extend_parameters` VARCHAR(1024) NOT NULL COMMENT '连接扩展参数.json格式存储',
    `url` VARCHAR(2048) NULL COMMENT '资源链接URL',
    `description` VARCHAR(255) NOT NULL COMMENT '资源描述信息',
    `is_enable` tinyint(4) NOT NULL COMMENT '0关闭，1开启',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '数据源信息表';

-- TODO Implement basic CURD.
DROP TABLE IF EXISTS `conversion_strategy`;
CREATE TABLE conversion_strategy(
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `strategy_code` VARCHAR(16) NOT NULL COMMENT '策略编码',
    `title` VARCHAR(64) NOT NULL COMMENT '策略名称',
    `step_code` VARCHAR(64) NOT NULL COMMENT '步骤编码',
    `step_order` VARCHAR(64) NOT NULL COMMENT '步骤顺序',
    `description` VARCHAR(255) NOT NULL COMMENT '描述信息',
    `is_enable` tinyint(4) NOT NULL COMMENT '0关闭，1开启',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '转换策略信息表';

DROP TABLE IF EXISTS `plan_info`;
CREATE TABLE plan_info(
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `plan_code` VARCHAR(64) NOT NULL COMMENT '方案编码',
    `title` VARCHAR(64) NOT NULL COMMENT '名称',
    `execution_mode` INT(11) NOT NULL COMMENT '单次，循环',
    `started_timestamp` bigint(20) NOT NULL COMMENT '启动时间戳',
    `execution_corn` VARCHAR(64) NOT NULL COMMENT '执行corn表达式',
    `execution_count` INT(11) NOT NULL COMMENT '执行次数',
    `is_executable` tinyint(4) NOT NULL COMMENT '0可执行，1执行中',
    `description` VARCHAR(255) NOT NULL COMMENT '资源描述信息',
    `is_enable` tinyint(4) NOT NULL COMMENT '0关闭，1开启',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '方案信息表';
