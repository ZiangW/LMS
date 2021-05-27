DROP TABLE IF EXISTS `BSM`.`returnbook`;
CREATE TABLE `BSM`.`returnbook` (
  `return_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '归还id',
  `return_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `return_book_id` int(11) NOT NULL DEFAULT '0' COMMENT '图书id',
  `return_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '归还日期',
  `return_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '归还状态',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `cuser` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建用户id',
  `muser` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改用户id',
  `remark` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`return_id`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4 COMMENT ='归还记录表';

-- INSERT INTO `BSM`.`returnbook` (`return_id`, `return_user_id`, `return_book_id`, `return_date`, `return_status`) VALUES ('1', '2', '2', '2021-4-21', '1');
-- INSERT INTO `BSM`.`returnbook` (`return_id`, `return_user_id`, `return_book_id`, `return_date`, `return_status`) VALUES ('2', '1', '2', '2021-4-21', '1');
-- INSERT INTO `BSM`.`returnbook` (`return_id`, `return_user_id`, `return_book_id`, `return_date`, `return_status`) VALUES ('3', '2', '3', '2021-4-25', '1');
