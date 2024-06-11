/*
Navicat MySQL Data Transfer

Source Server         : 毕设
Source Server Version : 80020
Source Host           : localhost:3306
Source Database       : bigdata

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2022-03-22 15:52:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for covid19_1
-- ----------------------------
DROP TABLE IF EXISTS `covid19_1`;
CREATE TABLE `covid19_1` (
  `datetime` varchar(20) NOT NULL DEFAULT '',
  `currentConfirmedCount` bigint DEFAULT '0',
  `confirmedCount` bigint DEFAULT '0',
  `suspectedCount` bigint DEFAULT '0',
  `curedCount` bigint DEFAULT '0',
  `deadCount` bigint DEFAULT '0',
  PRIMARY KEY (`datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for covid19_2
-- ----------------------------
DROP TABLE IF EXISTS `covid19_2`;
CREATE TABLE `covid19_2` (
  `datetime` varchar(20) NOT NULL DEFAULT '',
  `locationId` int NOT NULL DEFAULT '0',
  `provinceShortName` varchar(20) DEFAULT '',
  `cityName` varchar(20) DEFAULT '',
  `currentConfirmedCount` int DEFAULT '0',
  `confirmedCount` int DEFAULT '0',
  `suspectedCount` int DEFAULT '0',
  `curedCount` int DEFAULT '0',
  `deadCount` int DEFAULT '0',
  `pid` int DEFAULT '0',
  PRIMARY KEY (`datetime`,`locationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for covid19_3
-- ----------------------------
DROP TABLE IF EXISTS `covid19_3`;
CREATE TABLE `covid19_3` (
  `dateId` varchar(20) NOT NULL DEFAULT '',
  `confirmedIncr` bigint DEFAULT '0',
  `confirmedCount` bigint DEFAULT '0',
  `suspectedCount` bigint DEFAULT '0',
  `curedCount` bigint DEFAULT '0',
  `deadCount` bigint DEFAULT '0',
  PRIMARY KEY (`dateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for covid19_4
-- ----------------------------
DROP TABLE IF EXISTS `covid19_4`;
CREATE TABLE `covid19_4` (
  `datetime` varchar(20) NOT NULL DEFAULT '',
  `provinceShortName` varchar(20) NOT NULL DEFAULT '',
  `pid` int DEFAULT '0',
  `confirmedCount` bigint DEFAULT '0',
  PRIMARY KEY (`datetime`,`provinceShortName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for covid19_5
-- ----------------------------
DROP TABLE IF EXISTS `covid19_5`;
CREATE TABLE `covid19_5` (
  `datetime` varchar(20) NOT NULL DEFAULT '',
  `locationId` int NOT NULL DEFAULT '0',
  `provinceShortName` varchar(20) DEFAULT '',
  `cityName` varchar(20) DEFAULT '',
  `currentConfirmedCount` int DEFAULT '0',
  `confirmedCount` int DEFAULT '0',
  `suspectedCount` int DEFAULT '0',
  `curedCount` int DEFAULT '0',
  `deadCount` int DEFAULT '0',
  `pid` int DEFAULT '0',
  PRIMARY KEY (`datetime`,`locationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for covid19_wz
-- ----------------------------
DROP TABLE IF EXISTS `covid19_wz`;
CREATE TABLE `covid19_wz` (
  `name` varchar(12) NOT NULL DEFAULT '',
  `cg` int DEFAULT '0',
  `xb` int DEFAULT '0',
  `jz` int DEFAULT '0',
  `xh` int DEFAULT '0',
  `xq` int DEFAULT '0',
  `kc` int DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_offset
-- ----------------------------
DROP TABLE IF EXISTS `t_offset`;
CREATE TABLE `t_offset` (
  `topic` varchar(255) NOT NULL,
  `partition` int NOT NULL,
  `groupid` varchar(255) NOT NULL,
  `offset` bigint DEFAULT NULL,
  PRIMARY KEY (`topic`,`partition`,`groupid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
