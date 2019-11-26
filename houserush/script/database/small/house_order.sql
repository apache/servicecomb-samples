# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.44)
# Database: house_order
# Generation Time: 2019-11-20 01:39:03 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP DATABASE IF EXISTS `house_order`;

CREATE DATABASE `house_order` DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

USE `house_order`;

# Dump of table favorites
# ------------------------------------------------------------

DROP TABLE IF EXISTS `favorites`;

CREATE TABLE `favorites` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `house_order_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table house_orders
# ------------------------------------------------------------

DROP TABLE IF EXISTS `house_orders`;

CREATE TABLE `house_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sale_id` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `house_id` int(11) DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ordered_at` timestamp(4) NULL DEFAULT NULL,
  `created_at` timestamp(4) NULL DEFAULT NULL,
  `updated_at` timestamp(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

LOCK TABLES `house_orders` WRITE;
/*!40000 ALTER TABLE `house_orders` DISABLE KEYS */;

INSERT INTO `house_orders` (`id`, `sale_id`, `customer_id`, `house_id`, `state`, `ordered_at`, `created_at`, `updated_at`)
VALUES
	(1003,100,NULL,100,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1004,100,NULL,101,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1005,100,NULL,102,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1006,100,NULL,103,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1007,100,NULL,104,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1008,100,NULL,105,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1009,100,NULL,106,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1010,100,NULL,107,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1011,100,NULL,108,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1012,100,NULL,109,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1013,100,NULL,110,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1014,100,NULL,111,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1015,100,NULL,112,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1016,100,NULL,113,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1017,100,NULL,114,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1018,100,NULL,115,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1019,100,NULL,116,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1020,100,NULL,117,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1179,101,NULL,120,'new',NULL,'2019-08-12 03:55:25.0000',NULL),
	(1180,101,NULL,121,'new',NULL,'2019-08-12 03:55:25.0000',NULL);

/*!40000 ALTER TABLE `house_orders` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sale_qualification
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sale_qualification`;

CREATE TABLE `sale_qualification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `sale_id` int(11) DEFAULT NULL,
  `qualification_count` int(11) DEFAULT NULL,
  `order_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `hashkey` (`customer_id`,`sale_id`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sale_qualification` WRITE;
/*!40000 ALTER TABLE `sale_qualification` DISABLE KEYS */;

INSERT INTO `sale_qualification` (`id`, `customer_id`, `sale_id`, `qualification_count`, `order_count`)
VALUES
	(1,100,100,20,0),
	(2,101,100,20,0),
	(3,102,100,20,0),
	(4,103,100,20,0),
	(5,104,100,20,0),
	(6,105,100,20,0),
	(7,106,100,20,0),
	(8,107,100,20,0),
	(9,108,100,20,0),
	(10,109,100,20,0);

/*!40000 ALTER TABLE `sale_qualification` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sales
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sales`;

CREATE TABLE `sales` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `state` varchar(20) DEFAULT NULL,
  `begin_at` timestamp(4) NULL DEFAULT NULL,
  `end_at` timestamp(4) NULL DEFAULT NULL,
  `realestate_id` int(11) DEFAULT NULL,
  `deleted_at` timestamp(4) NULL DEFAULT NULL,
  `created_at` timestamp(4) NULL DEFAULT NULL,
  `updated_at` timestamp(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;

INSERT INTO `sales` (`id`, `state`, `begin_at`, `end_at`, `realestate_id`, `deleted_at`, `created_at`, `updated_at`)
VALUES
	(100,'published','2019-11-18 08:00:00.0000','2019-11-24 08:00:00.0000',100,NULL,'2019-08-12 11:29:21.2210','2019-11-20 09:13:27.5560'),
	(101,'published','2019-12-01 18:00:00.0000','2019-12-02 23:59:59.0000',101,NULL,'2019-08-12 11:29:21.2210','2019-08-12 11:29:21.2210');

/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
