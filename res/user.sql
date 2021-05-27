DROP TABLE IF EXISTS `BSM`.`user`;
CREATE TABLE `BSM`.`user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(45) NOT NULL DEFAULT '' COMMENT '用户名称',
  `user_pwd` varchar(45) NOT NULL DEFAULT '' COMMENT '用户密码',
  `user_email` varchar(45) NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `user_type` varchar(45) NOT NULL DEFAULT '' COMMENT '用户类型',
  `admin_id` int(11) NOT NULL DEFAULT '1' COMMENT '管理员id',
  `user_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户状态',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `cuser` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建用户id',
  `muser` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改用户id',
  `remark` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY (`user_name`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

INSERT INTO `BSM`.`user` (`user_id`, `user_name`, `user_pwd`, `user_email`, `user_type`, `admin_id`, `user_status`) VALUES ('1', 'a', '123456', 'a@ke.com', 'Bronze', '1', '1');
INSERT INTO `BSM`.`user` (`user_id`, `user_name`, `user_pwd`, `user_email`, `user_type`, `admin_id`, `user_status`) VALUES ('2', 'b', '123456', 'b@ke.com', 'Silver', '1', '1');
INSERT INTO `BSM`.`user` (`user_id`, `user_name`, `user_pwd`, `user_email`, `user_type`, `admin_id`, `user_status`) VALUES ('3', 'c', '123456', 'c@ke.com', 'Gold', '2', '1');
INSERT INTO `BSM`.`user` (`user_id`, `user_name`, `user_pwd`, `user_email`, `user_type`, `admin_id`, `user_status`) VALUES ('4', 'd', '123456', 'd@ke.com', 'Platinum', '2', '1');

select * from BSM.user;