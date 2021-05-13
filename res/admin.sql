DROP TABLE IF EXISTS `BSM`.`admin`;
CREATE TABLE `BSM`.`admin` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(45) DEFAULT NULL,
  `admin_pwd` varchar(45) DEFAULT NULL,
  `admin_email` varchar(45) DEFAULT NULL,
  `admin_status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB;
INSERT INTO `BSM`.`admin` VALUES ('1', 'wza', '1234', 'wza@ke.com', '1');

