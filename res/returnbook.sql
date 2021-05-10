DROP TABLE IF EXISTS `BSM`.`returnbook`;
CREATE TABLE `BSM`.`returnbook` (
  `return_id` int(11) NOT NULL AUTO_INCREMENT,
  `return_user_id` int(11) DEFAULT NULL,
  `return_book_id` int(11) DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `return_status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`return_id`),
  CONSTRAINT `return_book_id` FOREIGN KEY (`return_user_id`) REFERENCES `book` (`book_id`),
  CONSTRAINT `return_user_id` FOREIGN KEY (`return_user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB;

INSERT INTO `BSM`.`returnbook` VALUES ('1', '2', '2', '2021-4-21', '1');
INSERT INTO `BSM`.`returnbook` VALUES ('2', '1', '2', '2021-4-21', '1');
INSERT INTO `BSM`.`returnbook` VALUES ('3', '2', '3', '2021-4-25', '1');
