DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name` varchar(64) NOT NULL COMMENT '用户名',
    `password` varchar(64) NOT NULL COMMENT '用户密码',
    `email` varchar(64) NOT NULL COMMENT '用户邮箱',
    `status` int(11) NOT NULL COMMENT '状态 0初始化，1已启用，2未启用',
    `create_time` datetime NOT NULL COMMENT '创建时间',
    `modify_time` datetime NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
);