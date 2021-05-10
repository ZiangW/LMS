DROP TABLE IF EXISTS `BSM`.`user`;
CREATE TABLE `BSM`.`user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `user_pwd` varchar(45) DEFAULT NULL,
  `user_email` varchar(45) DEFAULT NULL,
  `user_type` varchar(45) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `user_status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB;

INSERT INTO `BSM`.`user` VALUES ('1', 'a', '123456', 'a@ke.com', 'Bronze', '1', '1');
INSERT INTO `BSM`.`user` VALUES ('2', 'b', '123456', 'b@ke.com', 'Silver', '1', '1');
INSERT INTO `BSM`.`user` VALUES ('3', 'c', '123456', 'c@ke.com', 'Gold', '2', '1');
INSERT INTO `BSM`.`user` VALUES ('4', 'd', '123456', 'd@ke.com', 'Platinum', '2', '1');

select * from BSM.user;