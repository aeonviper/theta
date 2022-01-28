/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

create sequence `entitySequence`;

CREATE TABLE IF NOT EXISTS `person` (
  `id` bigint(20) unsigned NOT NULL,
  `storageMapData` longtext DEFAULT NULL CHECK (json_valid(`storageMapData`)),
  `creatorId` bigint(20) unsigned DEFAULT NULL,
  `created` timestamp NULL DEFAULT NULL,
  `creator` longtext DEFAULT NULL CHECK (json_valid(`creator`)),
  `editorId` bigint(20) unsigned DEFAULT NULL,
  `edited` timestamp NULL DEFAULT NULL,
  `editor` longtext DEFAULT NULL CHECK (json_valid(`editor`)),
  `name` tinytext NOT NULL,
  `email` varchar(256) NOT NULL,
  `password` tinytext DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `image` tinytext DEFAULT NULL,
  `attachmentListData` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `roleSetData` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`roleSetData`)),
  `birthDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_person_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `shopId` bigint(20) unsigned NOT NULL,
  `slug` tinytext NOT NULL,
  `name` tinytext NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `foreignKey_product_shopId` (`shopId`),
  CONSTRAINT `foreignKey_product_shopId` FOREIGN KEY (`shopId`) REFERENCES `shop` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `shop` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `slug` varchar(128) NOT NULL,
  `name` tinytext NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_shop_slug` (`slug`)
) ENGINE=InnoDB AUTO_INCREMENT=29216 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `shopSetting` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `shopId` bigint(20) unsigned NOT NULL,
  `value` tinytext NOT NULL,
  PRIMARY KEY (`id`),
  KEY `foreignKey_shopSetting_shopId` (`shopId`),
  CONSTRAINT `foreignKey_shopSetting_shopId` FOREIGN KEY (`shopId`) REFERENCES `shop` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `template` (
  `id` bigint(20) unsigned NOT NULL,
  `storageMapData` longtext DEFAULT NULL CHECK (json_valid(`storageMapData`)),
  `creatorId` bigint(20) unsigned DEFAULT NULL,
  `created` timestamp NULL DEFAULT NULL,
  `creator` longtext DEFAULT NULL CHECK (json_valid(`creator`)),
  `editorId` bigint(20) unsigned DEFAULT NULL,
  `edited` timestamp NULL DEFAULT NULL,
  `editor` longtext DEFAULT NULL CHECK (json_valid(`editor`)),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
