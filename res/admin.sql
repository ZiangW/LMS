DROP TABLE IF EXISTS `BSM`.`admin`;
CREATE TABLE `BSM`.`admin` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `admin_name` varchar(45) NOT NULL DEFAULT '' COMMENT '管理员名称',
  `admin_pwd` varchar(45) NOT NULL DEFAULT '' COMMENT '管理员密码',
  `admin_email` varchar(45) NOT NULL DEFAULT '' COMMENT '管理员邮箱',
  `admin_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '管理员状态',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `cuser` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建用户id',
  `muser` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改用户id',
  `remark` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY (`admin_name`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4 COMMENT ='管理员表';

INSERT INTO `BSM`.`admin` (`admin_id`, `admin_name`, `admin_pwd`, `admin_email`, `admin_status`) VALUES ('1', 'wza', '1234', 'wza@ke.com', '1');

