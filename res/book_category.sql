DROP TABLE IF EXISTS `BSM`.`book`;
DROP TABLE IF EXISTS `BSM`.`book_category`;
CREATE TABLE `BSM`.`book_category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图书类别id',
  `category_name` varchar(20) NOT NULL DEFAULT '' COMMENT '类别名称',
  `category_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '类别状态',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `cuser` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建用户id',
  `muser` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改用户id',
  `remark` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY (`category_name`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4 COMMENT ='图书类别表';

INSERT INTO `BSM`.`book_category` (`category_id`, `category_name`, `category_status`) VALUES ('1', '科幻', '1');
INSERT INTO `BSM`.`book_category` (`category_id`, `category_name`, `category_status`) VALUES ('2', '外国经典文学', '1');
INSERT INTO `BSM`.`book_category` (`category_id`, `category_name`, `category_status`) VALUES ('3', '中国现代文学', '1');

select * from BSM.book_category;