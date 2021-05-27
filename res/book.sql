DROP TABLE IF EXISTS `BSM`.`book`;
CREATE TABLE `BSM`.`book` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图书id',
  `book_name` varchar(45) NOT NULL DEFAULT '' COMMENT '图书名',
  `book_author` varchar(45) NOT NULL DEFAULT '' COMMENT '作者',
  `book_category` int(11) NOT NULL DEFAULT '0' COMMENT '图书类别',
  `book_count` int(11) NOT NULL DEFAULT '0' COMMENT '图书余量',
  `book_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '图书状态',
  `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `cuser` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建用户id',
  `muser` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改用户id',
  `remark` varchar(64) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`book_id`),
  UNIQUE KEY (`book_name`)
)
ENGINE=InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARSET = utf8mb4 COMMENT ='图书表';


INSERT INTO `BSM`.`book` (`book_id`, `book_name`, `book_author`, `book_category`, `book_count`, `book_status`) VALUES ('1', '复活', '列夫.托尔斯泰', '2', '1', '1');
INSERT INTO `BSM`.`book` (`book_id`, `book_name`, `book_author`, `book_category`, `book_count`, `book_status`) VALUES ('2', '三体', '刘慈欣', '1', '3', '1');
INSERT INTO `BSM`.`book` (`book_id`, `book_name`, `book_author`, `book_category`, `book_count`, `book_status`) VALUES ('3', '平凡的世界', '路遥', '3', '2', '1');

select * from BSM.book;