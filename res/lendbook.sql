DROP TABLE IF EXISTS `BSM`.`lendbook`;
CREATE TABLE `BSM`.`lendbook` (
  `lend_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '借书id',
  `lend_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `lend_book_id` int(11) NOT NULL DEFAULT '0' COMMENT '图书id',
  `lend_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅日期',
  `lend_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '借阅状态',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `cuser` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建用户id',
  `muser` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改用户id',
  `remark` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`lend_id`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4 COMMENT ='借阅记录表';

INSERT INTO `BSM`.`lendbook` (`lend_id`, `lend_user_id`, `lend_book_id`, `lend_date`, `lend_status`) VALUES ('1', '1', '1', '2021-4-10', '1');
INSERT INTO `BSM`.`lendbook` (`lend_id`, `lend_user_id`, `lend_book_id`, `lend_date`, `lend_status`) VALUES ('2', '2', '2', '2021-4-11', '1');
INSERT INTO `BSM`.`lendbook` (`lend_id`, `lend_user_id`, `lend_book_id`, `lend_date`, `lend_status`) VALUES ('3', '2', '3', '2021-4-12', '1');
