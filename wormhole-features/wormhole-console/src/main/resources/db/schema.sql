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
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(64) NOT NULL COMMENT '用户密码',
    `email` VARCHAR(64) NOT NULL COMMENT '用户邮箱',
    `enable` tinyint(4) NOT NULL COMMENT '状态 0未启用，1已启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `datasource_info`;

CREATE TABLE datasource_info(
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title` VARCHAR(64) NOT NULL COMMENT '数据源名字',
    `genre` INT(11) NOT NULL COMMENT '数据源类型',
    `host` VARCHAR(16) NOT NULL COMMENT '数据源IP',
    `port` INT(11) NOT NULL COMMENT '数据源端口',
    `catalog` VARCHAR(64) NOT NULL COMMENT '数据库名称',
    `username` VARCHAR(64) NOT NULL COMMENT '链接用户名',
    `password` VARCHAR(64) NOT NULL COMMENT '链接用户密码',
    `extend_parameters` VARCHAR(1024) NOT NULL COMMENT '连接扩展参数.json格式存储',
    `url` VARCHAR(2048) NULL COMMENT '资源链接URL',
    `description` VARCHAR(1024) NOT NULL COMMENT '资源描述信息',
    `enable` tinyint(4) NOT NULL COMMENT '状态 0未启用，1已启用',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '数据源信息表';
