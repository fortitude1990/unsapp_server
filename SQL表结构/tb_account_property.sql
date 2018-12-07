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

 Date: 07/12/2018 13:58:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_account_property
-- ----------------------------
DROP TABLE IF EXISTS `tb_account_property`;
CREATE TABLE `tb_account_property` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountId` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `totalProperty` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `availableProperty` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `monthlySpending` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `monthlyIncome` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `payPwd` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `updateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `payPwdErrorNo` int(11) DEFAULT NULL,
  `yesterdayIncome` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '昨日收入',
  `yesterdaySpending` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '昨日花费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
