-- 创建数据库
CREATE DATABASE IF NOT EXISTS `uu` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `uu`;

-- 部门表 - 修正字段名
CREATE TABLE IF NOT EXISTS `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '部门名称',
  `code` varchar(50) DEFAULT NULL COMMENT '部门编码',
  `description` varchar(255) DEFAULT NULL COMMENT '部门描述',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父部门ID',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 用户表 - 修正字段名
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '角色：ADMIN-管理员，USER-普通用户',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 办公用品分类表 - 修正字段名
CREATE TABLE IF NOT EXISTS `supply_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='办公用品分类表';

-- 办公用品表 - 修正字段名
CREATE TABLE IF NOT EXISTS `supply_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `item_name` varchar(100) NOT NULL COMMENT '用品名称',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `specification` varchar(255) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `price` decimal(10,2) DEFAULT 0.00 COMMENT '单价',
  `stock_quantity` int(11) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `min_stock` int(11) DEFAULT 0 COMMENT '最低库存',
  `description` text COMMENT '描述',
  `image` varchar(255) DEFAULT NULL COMMENT '图片',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='办公用品表';

-- 申领申请表 - 修正字段名
CREATE TABLE IF NOT EXISTS `supply_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `application_no` varchar(50) NOT NULL COMMENT '申请单号',
  `user_id` bigint(20) NOT NULL COMMENT '申请人ID',
  `dept_id` bigint(20) NOT NULL COMMENT '申请部门ID',
  `title` varchar(200) NOT NULL COMMENT '申请标题',
  `reason` text COMMENT '申请原因',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING-待审核，APPROVED-已批准，REJECTED-已拒绝，COMPLETED-已完成',
  `approver_id` bigint(20) DEFAULT NULL COMMENT '审批人ID',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(500) DEFAULT NULL COMMENT '审批备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_application_no` (`application_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申领申请表';

-- 插入默认数据
INSERT INTO `department` (`id`, `name`, `code`, `description`, `parent_id`, `sort`, `status`) VALUES
(1, '总经理办公室', 'GM', '总经理办公室', 0, 1, 1),
(2, '人力资源部', 'HR', '人力资源部', 0, 2, 1),
(3, '财务部', 'FIN', '财务部', 0, 3, 1),
(4, '技术部', 'TECH', '技术部', 0, 4, 1),
(5, '市场部', 'MKT', '市场部', 0, 5, 1)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `code` = VALUES(`code`);

INSERT INTO `supply_category` (`id`, `name`, `description`, `sort`, `status`) VALUES
(1, '文具用品', '各类文具、纸张等', 1, 1),
(2, '办公设备', '打印机、电脑等设备', 2, 1),
(3, '清洁用品', '清洁工具、耗材等', 3, 1),
(4, '办公家具', '桌椅、柜子等家具', 4, 1)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `description` = VALUES(`description`);

INSERT INTO `supply_item` (`id`, `item_name`, `category_id`, `specification`, `unit`, `price`, `stock_quantity`, `min_stock`, `description`, `status`) VALUES
(1, 'A4打印纸', 1, '80g 500张/包', '包', 25.00, 100, 20, '标准A4打印纸', 1),
(2, '签字笔', 1, '黑色 0.5mm', '支', 2.50, 200, 50, '办公用签字笔', 1),
(3, '笔记本', 1, 'A5 100页', '本', 8.00, 150, 30, '办公笔记本', 1),
(4, '打印机墨盒', 2, 'HP 88A', '个', 120.00, 10, 5, 'HP打印机墨盒', 1),
(5, '办公椅', 4, '人体工学', '把', 800.00, 5, 2, '人体工学办公椅', 1)
ON DUPLICATE KEY UPDATE `item_name` = VALUES(`item_name`), `price` = VALUES(`price`);

-- 插入默认管理员账号（密码：123456）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `role`, `dept_id`, `status`) VALUES
('admin', '$2a$10$9U9be6r9GygwPJAKhQppYu0zjn9Fi0kx3O/GkVUwVnVMMrJ1fr4Ia', '系统管理员', 'ADMIN', 1, 1)
ON DUPLICATE KEY UPDATE `password` = VALUES(`password`);

-- 检查表结构
SHOW TABLES;
SELECT 'department' as table_name, COUNT(*) as record_count FROM department
UNION ALL
SELECT 'sys_user' as table_name, COUNT(*) as record_count FROM sys_user
UNION ALL
SELECT 'supply_category' as table_name, COUNT(*) as record_count FROM supply_category
UNION ALL
SELECT 'supply_item' as table_name, COUNT(*) as record_count FROM supply_item
UNION ALL
SELECT 'supply_application' as table_name, COUNT(*) as record_count FROM supply_application;
