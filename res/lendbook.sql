DROP TABLE IF EXISTS `BSM`.`lendbook`;
CREATE TABLE `BSM`.`lendbook` (
  `lend_id` int(11) NOT NULL AUTO_INCREMENT,
  `lend_user_id` int(11) DEFAULT NULL,
  `lend_book_id` int(11) DEFAULT NULL,
  `lend_date` date DEFAULT NULL,
  `lend_status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`lend_id`),
  CONSTRAINT `lend_book_id` FOREIGN KEY (`lend_book_id`) REFERENCES `book` (`book_id`),
  CONSTRAINT `lend_user_id` FOREIGN KEY (`lend_user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

INSERT INTO `BSM`.`lendbook` VALUES ('1', '1', '1', '2021-4-10', '1');
INSERT INTO `BSM`.`lendbook` VALUES ('2', '2', '2', '2021-4-11', '1');
INSERT INTO `BSM`.`lendbook` VALUES ('3', '2', '3', '2021-4-12', '1');
