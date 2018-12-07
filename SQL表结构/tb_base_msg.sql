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

 Date: 07/12/2018 13:58:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_base_msg
-- ----------------------------
DROP TABLE IF EXISTS `tb_base_msg`;
CREATE TABLE `tb_base_msg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '名字',
  `sex` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `identityId` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号码',
  `birthday` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '生日',
  `tel` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '电话号码',
  `pwd` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '登陆密码',
  `accountId` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '账户ID',
  `nickname` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `gesturesPwd` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '手势密码',
  `headPortraitImage` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `pwdErrorNum` int(11) DEFAULT NULL COMMENT '密码输错次数',
  `loginTime` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '最后一次登陆时间',
  `constellation` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '星座',
  `height` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '身高',
  `weight` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '体重',
  `region` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '地区',
  `professional` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '职业',
  `income` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '收入',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

SET FOREIGN_KEY_CHECKS = 1;
