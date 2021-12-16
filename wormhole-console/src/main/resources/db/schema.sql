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

-- TODO Implement basic CURD.

DROP TABLE IF EXISTS `database_info`;
CREATE TABLE database_info (
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `title` VARCHAR(32) NOT NULL COMMENT '名称',
    `host` VARCHAR(15) NOT NULL COMMENT 'IP',
    `port` INT(11) NOT NULL COMMENT '端口',
    `type` tinyint(4) NOT NULL COMMENT '类型0:MySQL 1:Oracle 2:SQLServer 3:PostgreSQL',
    `username` VARCHAR(32) NOT NULL COMMENT '链接用户名',
    `password` VARCHAR(32) NOT NULL COMMENT '链接密码',
    `description` VARCHAR(255) NOT NULL COMMENT '描述',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_title`(`title`),
    UNIQUE INDEX `uk_host_port`(`host`, `port`)
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '数据库信息';

DROP TABLE IF EXISTS `datasource_info`;
CREATE TABLE datasource_info(
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `database_id` INT(11) NOT NULL COMMENT '所属数据库ID',
    `schema` VARCHAR(32) NOT NULL COMMENT '数据源名称',
    `username` VARCHAR(32) NOT NULL COMMENT '数据源用户名',
    `password` VARCHAR(32) NOT NULL COMMENT '数据源密码',
    `extend_parameters` VARCHAR(255) NOT NULL COMMENT '扩展参数',
    `description` VARCHAR(255) NOT NULL COMMENT '描述',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_database_id_schema_username_password`(`database_id`, `schema`, `username`, `password`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '数据源信息';

DROP TABLE IF EXISTS `plan_info`;
CREATE TABLE plan_info(
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `title` VARCHAR(32) NOT NULL COMMENT '名称',
    `code` VARCHAR(32) NOT NULL COMMENT '方案编码',
    `execution_mode` tinyint(4) NOT NULL COMMENT '执行模式0:单次 1:循环',
    `execution_corn` VARCHAR(10) NOT NULL COMMENT '执行corn表达式',
    `execution_count` INT(11) NOT NULL COMMENT '执行次数',
    `effective_date` datetime(0) NOT NULL COMMENT '生效时间',
    `is_execute` tinyint(4) NOT NULL COMMENT '0未执行，1执行中',
    `is_enable` tinyint(4) NOT NULL COMMENT '0关闭，1开启',
    `description` VARCHAR(255) NOT NULL COMMENT '描述',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '方案信息表';

DROP TABLE IF EXISTS `task_info`;
CREATE TABLE task_info(
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `plan_id` INT(11) NOT NULL COMMENT '所属方案id',
    `title` VARCHAR(32) NOT NULL COMMENT '任务名字',
    `code` VARCHAR(32) NOT NULL COMMENT '任务编码',
    `order` INT(11) NOT NULL COMMENT '步骤顺序',
    `is_enable` tinyint(4) NOT NULL COMMENT '0关闭，1开启',
    `is_execute` tinyint(4) NOT NULL COMMENT '0未执行，1执行中',
    `description` VARCHAR(255) NOT NULL COMMENT '资源描述信息',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '任务信息表';

DROP TABLE IF EXISTS `plan_task_linking`;
CREATE TABLE plan_task_linking(
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `plan_id` INT(11) NOT NULL COMMENT '方案id',
    `task_id` INT(11) NOT NULL COMMENT '任务id',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_plan_id_task_id` (`plan_id`, `task_id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '节点映射表';

DROP TABLE IF EXISTS `data_node_mapping`;
CREATE TABLE data_node_mapping (
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `task_id` INT(11) NOT NULL COMMENT '任务编码',
    `source_data_owner` VARCHAR(255) NOT NULL COMMENT '源数据持有者',
    `source_data_type` VARCHAR(32) NOT NULL COMMENT '多维度类型',
    `target_data_owner` VARCHAR(255) NOT NULL COMMENT '资源描述信息',
    `target_data_type` VARCHAR(32) NOT NULL COMMENT '多维度类型',
    `conversion_strategy_id` INT(11) NOT NULL COMMENT '转换策略ID',
    `description` VARCHAR(255) NOT NULL COMMENT '描述',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_task_id_source_data_owner_target_data_owner`(`task_id`, `source_data_owner`, `target_data_owner`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '节点映射表';

DROP TABLE IF EXISTS `conversion_strategy`;
CREATE TABLE conversion_strategy(
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `code` VARCHAR(32) NOT NULL COMMENT '策略编码',
    `action_code` VARCHAR(32) NOT NULL COMMENT '步骤编码',
    `action_order` INT(11) NOT NULL COMMENT '步骤顺序',
    `is_enable` tinyint(4) NOT NULL COMMENT '0关闭，1开启',
    `description` VARCHAR(255) NOT NULL COMMENT '描述信息',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_code`(`code`),
    UNIQUE INDEX `uk_code_action_code`(`code`, `action_code`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '转换策略信息表';

DROP TABLE IF EXISTS `plan_execution_log`;
CREATE TABLE plan_execution_log (
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `plan_batch` bigint(20) NOT NULL COMMENT '方案批次号',
    `plan_id` INT(11) NOT NULL COMMENT '关联方案ID',
    `status` INT(11) NOT NULL COMMENT '执行状态',
    `description` VARCHAR(255) NOT NULL COMMENT '执行描述',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_plan_batch` (`plan_batch`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '方案执行日志表';

DROP TABLE IF EXISTS `error_date_log`;
CREATE TABLE error_date_log (
    `id` INT(11) AUTO_INCREMENT COMMENT '主键',
    `plan_batch` bigint(20) NOT NULL COMMENT '所属方案批次号',
    `task_batch` bigint(20) NOT NULL COMMENT '任务批次号',
    `task_id` INT(11) NOT NULL COMMENT '所属任务ID',
    `source_date_owner` VARCHAR(255) NOT NULL COMMENT '源数据持有者',
    `error_code` VARCHAR(32) NOT NULL COMMENT '异常编码',
    `error_date` VARCHAR(1024) NOT NULL COMMENT '异常数据',
    `description` VARCHAR(1024) NOT NULL COMMENT '异常描述',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_task_batch`(`task_batch`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '异常数据日志表';
