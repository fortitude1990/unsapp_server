/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : unsapp_personal

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 07/12/2018 13:57:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_account_bankcard
-- ----------------------------
DROP TABLE IF EXISTS `tb_account_bankcard`;
CREATE TABLE `tb_account_bankcard` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountId` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bankNo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bankName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bankCode` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bankAboutMobile` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cardType` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '0:储蓄卡，1：信用卡，2：其他',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
