-- hugio_order_db.ORDERS definition

CREATE TABLE `ORDERS` (
  `ACTIVE` bit(1) NOT NULL,
  `TOTAL_PRICE` double DEFAULT NULL,
  `CREATED_AT` datetime(6) NOT NULL,
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `UPDATED_AT` datetime(6) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `CUSTOMER_NAME` varchar(255) DEFAULT NULL,
  `CUSTOMER_PHONE_NUMBER` varchar(255) DEFAULT NULL,
  `ORDER_CODE` varchar(255) DEFAULT NULL,
  `UPDATED_BY` varchar(255) DEFAULT NULL,
  `ORDER_STATUS` enum('CANCELED','DONE','PENDING') DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;


-- hugio_order_db.ORDER_STATISTIC_HISTORY definition

CREATE TABLE `ORDER_STATISTIC_HISTORY` (
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ACTIVE` bit(1) NOT NULL,
  `CREATED_AT` datetime(6) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `UPDATED_AT` datetime(6) NOT NULL,
  `UPDATED_BY` varchar(255) DEFAULT NULL,
  `STATISTIC_ANSWER` longtext,
  `STATISTIC_DATE` date DEFAULT NULL,
  `STATISTIC_QUESTION` longtext,
  `STATISTIC_TYPE` enum('ORDER','PRODUCT') DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;


-- hugio_order_db.ORDER_DETAIL definition

CREATE TABLE `ORDER_DETAIL` (
  `ACTIVE` bit(1) NOT NULL,
  `QUANTITY` int DEFAULT NULL,
  `CREATED_AT` datetime(6) NOT NULL,
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `ORDER_ID` bigint NOT NULL,
  `UPDATED_AT` datetime(6) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `PRODUCT_UID` varchar(255) DEFAULT NULL,
  `UPDATED_BY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3b610k97i15wuntetphqk09sm` (`ORDER_ID`),
  CONSTRAINT `FK3b610k97i15wuntetphqk09sm` FOREIGN KEY (`ORDER_ID`) REFERENCES `ORDERS` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4;