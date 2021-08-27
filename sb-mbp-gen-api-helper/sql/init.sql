/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : api_helper

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2021-08-27 16:12:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ah_class_code
-- ----------------------------
DROP TABLE IF EXISTS `ah_class_code`;
CREATE TABLE `ah_class_code` (
  `id` char(50) NOT NULL COMMENT '主键',
  `package_name` varchar(100) DEFAULT NULL COMMENT '包名',
  `class_name` varchar(50) DEFAULT NULL COMMENT '类名',
  `insert_time` varchar(20) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `ah_project_id` char(50) DEFAULT NULL COMMENT '项目ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_project` (`ah_project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='可用实体类';

-- ----------------------------
-- Table structure for ah_field
-- ----------------------------
DROP TABLE IF EXISTS `ah_field`;
CREATE TABLE `ah_field` (
  `id` char(50) NOT NULL COMMENT '主键ID',
  `type_name` varchar(100) DEFAULT NULL COMMENT '字段类型名',
  `generic_type_name` varchar(100) DEFAULT NULL COMMENT '泛型名',
  `field_name` varchar(50) DEFAULT NULL COMMENT '字段名',
  `insert_time` varchar(20) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `ah_class_code_id` char(50) DEFAULT NULL COMMENT '实体类ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_class_code` (`ah_class_code_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for ah_interface
-- ----------------------------
DROP TABLE IF EXISTS `ah_interface`;
CREATE TABLE `ah_interface` (
  `id` char(50) NOT NULL COMMENT '主键',
  `inter_no` varchar(50) DEFAULT NULL COMMENT '接口号',
  `http_url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `http_method` varchar(10) DEFAULT NULL COMMENT 'Http方法',
  `add_default_prefix` varchar(10) DEFAULT NULL COMMENT '是否有默认前缀',
  `inter_descr` varchar(200) DEFAULT NULL COMMENT '描述',
  `data_json` text COMMENT '配置JSON',
  `insert_time` varchar(20) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `ah_project_id` char(50) DEFAULT NULL COMMENT '项目ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_project` (`ah_project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='接口';

-- ----------------------------
-- Table structure for ah_project
-- ----------------------------
DROP TABLE IF EXISTS `ah_project`;
CREATE TABLE `ah_project` (
  `id` char(50) NOT NULL COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `descr` varchar(50) DEFAULT NULL COMMENT '描述',
  `url_prefix` varchar(50) DEFAULT NULL COMMENT 'URL前缀',
  `base_package` varchar(50) DEFAULT NULL COMMENT '基础包名',
  `insert_time` varchar(20) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='项目';
