DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(64) NOT NULL COMMENT '用户密码',
    `email` VARCHAR(64) NOT NULL COMMENT '用户邮箱',
    `status` tinyint(4) NOT NULL COMMENT '状态 0未启用，1已启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `datasource_info`;

CREATE TABLE datasource_info(
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(64) NOT NULL COMMENT '数据源名字',
    `type` INT(11) NOT NULL COMMENT '数据源类型',
    `ip` VARCHAR(64) NOT NULL COMMENT '数据源IP',
    `port` INT(11) NOT NULL COMMENT '数据源端口',
    `schema_name` VARCHAR(64) NOT NULL COMMENT '数据库名称',
    `username` VARCHAR(64) NOT NULL COMMENT '链接用户名',
    `password` VARCHAR(64) NOT NULL COMMENT '链接用户密码',
    `extend_parameters` VARCHAR(1024) NOT NULL COMMENT '连接扩展参数.json格式存储',
    `url` VARCHAR(2048) NULL COMMENT '资源链接URL',
    `description` VARCHAR(1024) NOT NULL COMMENT '资源描述信息',
    `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态 0未启用，1已启用',
    `operator` INT(11) NOT NULL COMMENT '操作员ID',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `modify_time` datetime(0) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '数据源信息表';
