DELETE FROM `user_info`;

INSERT INTO `user_info` (id, `user_name`, `password`, `email`, `status`, `create_time`, `modify_time`) VALUES
(1, 'jack', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.60'}, {ts '2012-11-19 18:30:30.60'}),
(2, 'rose', '123456', 'jacky7boy@163.com', 0, {ts '2012-11-19 18:30:30.60'}, {ts '2012-11-19 18:30:30.60'});
