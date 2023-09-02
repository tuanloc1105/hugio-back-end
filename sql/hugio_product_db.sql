-- hugio_product_db.CATEGORY definition

CREATE TABLE `CATEGORY` (
  `ACTIVE` bit(1) NOT NULL,
  `CREATED_AT` datetime(6) NOT NULL,
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `UPDATED_AT` datetime(6) NOT NULL,
  `CATEGORY_NAME` varchar(255) NOT NULL,
  `CATEGORY_UID` varchar(255) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `UPDATED_BY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;


-- hugio_product_db.PRODUCT definition

CREATE TABLE `PRODUCT` (
  `ACTIVE` bit(1) NOT NULL,
  `DISCOUNT` double DEFAULT NULL,
  `PRICE` double DEFAULT NULL,
  `CREATED_AT` datetime(6) NOT NULL,
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `UPDATED_AT` datetime(6) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `PRODUCT_DESCRIPTION` varchar(255) DEFAULT NULL,
  `PRODUCT_NAME` varchar(255) DEFAULT NULL,
  `PRODUCT_UID` varchar(255) NOT NULL,
  `RAW_PRODUCT_NAME` varchar(255) DEFAULT NULL,
  `UPDATED_BY` varchar(255) DEFAULT NULL,
  `PRODUCT_QR` longblob,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;


-- hugio_product_db.PRODUCT_CATEGORY definition

CREATE TABLE `PRODUCT_CATEGORY` (
  `ACTIVE` bit(1) NOT NULL,
  `CATEGORY_ID` bigint NOT NULL,
  `CREATED_AT` datetime(6) NOT NULL,
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` bigint NOT NULL,
  `UPDATED_AT` datetime(6) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `UPDATED_BY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3xw1sbaa29r534jvedimdd7md` (`CATEGORY_ID`),
  KEY `FKa7245ly271mb0crlhxwhhppsq` (`PRODUCT_ID`),
  CONSTRAINT `FK3xw1sbaa29r534jvedimdd7md` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `CATEGORY` (`ID`),
  CONSTRAINT `FKa7245ly271mb0crlhxwhhppsq` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4;


-- hugio_product_db.PRODUCT_DETAIL definition

CREATE TABLE `PRODUCT_DETAIL` (
  `ACTIVE` bit(1) NOT NULL,
  `CREATED_AT` datetime(6) NOT NULL,
  `ID` bigint NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` bigint DEFAULT NULL,
  `UPDATED_AT` datetime(6) NOT NULL,
  `CREATED_BY` varchar(255) DEFAULT NULL,
  `DETAIL_KEY` varchar(255) DEFAULT NULL,
  `DETAIL_VALUE` varchar(255) DEFAULT NULL,
  `UPDATED_BY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK23no4dthqi04i6b31ul8ppgaf` (`PRODUCT_ID`),
  CONSTRAINT `FK23no4dthqi04i6b31ul8ppgaf` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;