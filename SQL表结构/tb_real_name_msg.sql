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

 Date: 07/12/2018 13:58:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_real_name_msg
-- ----------------------------
DROP TABLE IF EXISTS `tb_real_name_msg`;
CREATE TABLE `tb_real_name_msg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountId` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '账号ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `identityId` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号码',
  `identityIdValidDate` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '身份证有效日期',
  `frontFaceOfIdCardPhoto` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '身份证正面照片',
  `reverseSideOfIdCardPhoto` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '身份证反面照片',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '0' COMMENT '0：审核中，1：审核通过，2：审核被拒绝',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
