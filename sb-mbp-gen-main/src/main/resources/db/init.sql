/*
Navicat MySQL Data Transfer

Source Server         : gen
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : gen

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2019-01-22 10:10:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for base_authority_resource
-- ----------------------------
DROP TABLE IF EXISTS `base_authority_resource`;
CREATE TABLE `base_authority_resource` (
  `id` char(50) NOT NULL,
  `authority_name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `base_resource_id` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_base_authority_resource_base_resource_id` (`base_resource_id`),
  CONSTRAINT `fk_base_authority_resource_base_resource_id` FOREIGN KEY (`base_resource_id`) REFERENCES `base_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色资源关系';

-- ----------------------------
-- Table structure for base_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `base_dictionary`;
CREATE TABLE `base_dictionary` (
  `id` char(50) NOT NULL,
  `dic_type` varchar(255) DEFAULT NULL COMMENT '数据字典类别编码',
  `dic_code` varchar(255) DEFAULT NULL COMMENT '数据字典代码',
  `dic_value` varchar(255) DEFAULT NULL COMMENT '数据字典中文值',
  `dic_discription` varchar(255) DEFAULT NULL COMMENT '数据字典描述',
  `parent_id` char(50) DEFAULT NULL COMMENT '父级ID',
  `current_level` int(11) DEFAULT NULL COMMENT '当前层级',
  `insert_time` varchar(255) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典';

-- ----------------------------
-- Table structure for base_organization
-- ----------------------------
DROP TABLE IF EXISTS `base_organization`;
CREATE TABLE `base_organization` (
  `id` char(50) NOT NULL,
  `code` varchar(255) DEFAULT NULL COMMENT '组织机构代码',
  `name` varchar(255) DEFAULT NULL COMMENT '组织机构名称',
  `description` varchar(255) DEFAULT NULL COMMENT '组织机构描述',
  `parent_id` char(50) DEFAULT NULL COMMENT '父级ID',
  `current_level` int(11) DEFAULT NULL COMMENT '当前层级',
  `insert_time` varchar(255) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织架构';

-- ----------------------------
-- Table structure for base_permission
-- ----------------------------
DROP TABLE IF EXISTS `base_permission`;
CREATE TABLE `base_permission` (
  `id` char(50) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '操作许可名称',
  `description` varchar(255) DEFAULT NULL COMMENT '操作许可描述',
  `system_code` varchar(255) DEFAULT NULL COMMENT '许可所属系统代码（只有在多系统联合配置权限时使用）',
  `http_type` varchar(255) DEFAULT NULL COMMENT '操作许可类别（例如：GET/POST/PUT/DELETE等）',
  `http_url` varchar(255) DEFAULT NULL COMMENT '访问URL',
  `function_categroy` varchar(255) DEFAULT NULL COMMENT '功能归类',
  `name_modified` int(11) DEFAULT NULL COMMENT '名称是否已更改（1：未更改  2：已更改）',
  `parent_id` char(50) DEFAULT NULL COMMENT '父级ID',
  `current_level` int(11) DEFAULT NULL COMMENT '当前层级',
  `allow_config` int(11) DEFAULT NULL COMMENT '是否允许配置（1：是   2：否）',
  `insert_time` varchar(255) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作许可（增删改查等）';

-- ----------------------------
-- Table structure for base_resource
-- ----------------------------
DROP TABLE IF EXISTS `base_resource`;
CREATE TABLE `base_resource` (
  `id` char(50) NOT NULL,
  `resource_type` varchar(255) DEFAULT NULL COMMENT '资源类别（PERMISSION：权限   CLIENT_TYPE：客户端应用    SUB_SYSTEM：子系统）',
  `name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `description` varchar(255) DEFAULT NULL COMMENT '资源描述',
  `system_code` varchar(255) DEFAULT NULL COMMENT '资源所属系统代码（只有在多系统联合配置权限时使用）',
  `identify` varchar(255) DEFAULT NULL COMMENT '资源特性标识（同一系统，同一类别内资源特性标识）',
  `parent_id` char(50) DEFAULT NULL COMMENT '父级ID',
  `current_level` int(11) DEFAULT NULL COMMENT '当前层级',
  `insert_time` varchar(255) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源（系统应用、页面资源等）';

-- ----------------------------
-- Table structure for base_resource_permission
-- ----------------------------
DROP TABLE IF EXISTS `base_resource_permission`;
CREATE TABLE `base_resource_permission` (
  `id` char(50) NOT NULL,
  `insert_time` varchar(255) DEFAULT NULL COMMENT '插入时间',
  `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间',
  `base_permission_id` char(50) DEFAULT NULL,
  `base_resource_id` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_base_resource_permission_base_permission_id` (`base_permission_id`),
  KEY `fk_base_resource_permission_base_resource_id` (`base_resource_id`),
  CONSTRAINT `fk_base_resource_permission_base_permission_id` FOREIGN KEY (`base_permission_id`) REFERENCES `base_permission` (`id`),
  CONSTRAINT `fk_base_resource_permission_base_resource_id` FOREIGN KEY (`base_resource_id`) REFERENCES `base_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源和许可关联';

-- ----------------------------
-- Table structure for base_user_organization
-- ----------------------------
DROP TABLE IF EXISTS `base_user_organization`;
CREATE TABLE `base_user_organization` (
  `id` char(50) NOT NULL,
  `user_id` char(50) DEFAULT NULL COMMENT '用户ID',
  `organization_code` varchar(255) DEFAULT NULL COMMENT '组织机构代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组织架构关系';

-- ----------------------------
-- Table structure for base_user_resource
-- ----------------------------
DROP TABLE IF EXISTS `base_user_resource`;
CREATE TABLE `base_user_resource` (
  `id` char(50) NOT NULL,
  `user_id` char(50) DEFAULT NULL COMMENT '用户ID',
  `base_resource_id` char(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_base_user_resource_base_resource_id` (`base_resource_id`),
  CONSTRAINT `fk_base_user_resource_base_resource_id` FOREIGN KEY (`base_resource_id`) REFERENCES `base_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户资源关系';

-- ----------------------------
-- Table structure for jhi_authority
-- ----------------------------
DROP TABLE IF EXISTS `jhi_authority`;
CREATE TABLE `jhi_authority` (
  `name` varchar(50) NOT NULL COMMENT '名称标识',
  `chinese_name` varchar(255) DEFAULT NULL COMMENT '中文名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jhi_date_time_wrapper
-- ----------------------------
DROP TABLE IF EXISTS `jhi_date_time_wrapper`;
CREATE TABLE `jhi_date_time_wrapper` (
  `id` char(50) NOT NULL,
  `instant` timestamp NULL DEFAULT NULL,
  `local_date_time` timestamp NULL DEFAULT NULL,
  `offset_date_time` timestamp NULL DEFAULT NULL,
  `zoned_date_time` timestamp NULL DEFAULT NULL,
  `local_time` time DEFAULT NULL,
  `offset_time` time DEFAULT NULL,
  `local_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jhi_persistent_audit_event
-- ----------------------------
DROP TABLE IF EXISTS `jhi_persistent_audit_event`;
CREATE TABLE `jhi_persistent_audit_event` (
  `event_id` char(50) NOT NULL,
  `principal` varchar(50) NOT NULL,
  `event_date` timestamp NULL DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`),
  KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jhi_persistent_audit_evt_data
-- ----------------------------
DROP TABLE IF EXISTS `jhi_persistent_audit_evt_data`;
CREATE TABLE `jhi_persistent_audit_evt_data` (
  `event_id` char(50) NOT NULL,
  `name` varchar(150) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`event_id`,`name`),
  KEY `idx_persistent_audit_evt_data` (`event_id`),
  CONSTRAINT `fk_evt_pers_audit_evt_data` FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jhi_user
-- ----------------------------
DROP TABLE IF EXISTS `jhi_user`;
CREATE TABLE `jhi_user` (
  `id` char(50) NOT NULL,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(254) DEFAULT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(6) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_user_login` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for jhi_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `jhi_user_authority`;
CREATE TABLE `jhi_user_authority` (
  `user_id` char(50) NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jhi_authority
-- ----------------------------
INSERT INTO `jhi_authority` VALUES ('ROLE_ADMIN', '管理员', '管理员');
INSERT INTO `jhi_authority` VALUES ('ROLE_USER', '普通用户', '普通用户');

-- ----------------------------
-- Records of jhi_user
-- ----------------------------
INSERT INTO `jhi_user` VALUES ('1', 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'System', 'System', 'system@localhost', '', '', 'en', null, null, 'system', null, null, 'system', null);
INSERT INTO `jhi_user` VALUES ('2', 'anonymoususer', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', 'Anonymous', 'User', 'anonymous@localhost', '', '', 'en', null, null, 'system', null, null, 'system', null);
INSERT INTO `jhi_user` VALUES ('3', 'admin', '$2a$10$PQkBezu.nvPOSenQXu/WxOMQtKj1j5ybjELKRfxr8uLeU8NCRBhDq', 'Administrator', null, 'admin@localhost.com', null, '', null, null, null, 'system', null, null, 'admin', '2019-01-22 02:29:28');

-- ----------------------------
-- Records of jhi_user_authority
-- ----------------------------
INSERT INTO `jhi_user_authority` VALUES ('1', 'ROLE_ADMIN');
INSERT INTO `jhi_user_authority` VALUES ('3', 'ROLE_ADMIN');
