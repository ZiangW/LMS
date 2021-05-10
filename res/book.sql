DROP TABLE IF EXISTS `BSM`.`book`;
CREATE TABLE `BSM`.`book` (
  `book_id` int(11) NOT NULL AUTO_INCREMENT,
  `book_name` varchar(45) DEFAULT NULL,
  `book_author` varchar(45) DEFAULT NULL,
  `book_category` int(11) DEFAULT NULL,
  `book_count` int(11) DEFAULT NULL,
  `book_status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`book_id`),
  KEY `book_category` (`book_category`),
  CONSTRAINT `book_category` FOREIGN KEY (`book_category`) REFERENCES `book_category` (`category_id`)
) ENGINE=InnoDB;
INSERT INTO `BSM`.`book` VALUES ('1', '复活', '列夫.托尔斯泰', '2', '1', '1');
INSERT INTO `BSM`.`book` VALUES ('2', '三体', '刘慈欣', '1', '3', '1');
INSERT INTO `BSM`.`book` VALUES ('3', '平凡的世界', '路遥', '3', '2', '1');

select * from BSM.book;