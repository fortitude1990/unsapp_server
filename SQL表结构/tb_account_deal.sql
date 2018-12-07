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

 Date: 07/12/2018 13:57:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_account_deal
-- ----------------------------
DROP TABLE IF EXISTS `tb_account_deal`;
CREATE TABLE `tb_account_deal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountId` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '交易人账号ID',
  `dealTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '交易时间',
  `amount` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '金额',
  `bankNo` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '银行卡号码',
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '交易人姓名',
  `toAccountId` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '转账：收款账号',
  `toBankNo` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '转账：收款银行卡',
  `toName` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '转账：收款人姓名',
  `dealType` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '0:充值，1：提现，2：转账',
  `des` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `orderNo` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '交易编号',
  `payType` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '支付类型 0:账户余额 1：银行卡',
  `transferType` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '0：转账到钱包；1：转账到银行卡',
  `bankName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `toBankName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `status` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '0:成功，1：失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
