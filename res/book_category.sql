DROP TABLE IF EXISTS `BSM`.`book`;
DROP TABLE IF EXISTS `BSM`.`book_category`;
CREATE TABLE `BSM`.`book_category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(20) DEFAULT NULL,
  `category_status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB;

INSERT INTO `BSM`.`book_category` VALUES ('1', '科幻', '1');
INSERT INTO `BSM`.`book_category` VALUES ('2', '外国经典文学', '1');
INSERT INTO `BSM`.`book_category` VALUES ('3', '中国现代文学', '1');

select * from BSM.book_category;