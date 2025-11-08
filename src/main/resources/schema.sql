-- 车险续保平台数据库表结构

-- 用户相关表
-- ============================================================

-- C端用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `openid` VARCHAR(128) NOT NULL UNIQUE COMMENT '微信openid',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `phone` VARCHAR(11) COMMENT '手机号',
  `age` INT COMMENT '年龄',
  `gender` TINYINT COMMENT '性别 0未知 1男 2女',
  `address` VARCHAR(255) COMMENT '地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_phone` (`phone`),
  INDEX `idx_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='C端用户表';

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `phone` VARCHAR(11) COMMENT '手机号',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0禁用 1启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_username` (`username`),
  INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
  `code` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码 如SUPER_ADMIN/ADMIN/SALESMAN',
  `description` VARCHAR(255) COMMENT '角色描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS `permission` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `code` VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
  `type` VARCHAR(20) NOT NULL COMMENT '权限类型 MENU/BUTTON',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
  `path` VARCHAR(255) COMMENT '页面路径',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `role_id` BIGINT NOT NULL,
  `permission_id` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  INDEX `idx_role_id` (`role_id`),
  INDEX `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 品牌车辆相关表
-- ============================================================

-- 品牌表（含经销商信息）
CREATE TABLE IF NOT EXISTS `brand` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '品牌名称',
  `logo` VARCHAR(255) COMMENT '品牌Logo',
  `dealer_name` VARCHAR(100) COMMENT '经销商名称',
  `dealer_address` VARCHAR(255) COMMENT '经销商地址',
  `dealer_contact_name` VARCHAR(50) COMMENT '经销商负责人姓名',
  `dealer_contact_phone` VARCHAR(11) COMMENT '经销商负责人手机号',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='品牌表（含经销商信息）';

-- 车辆表
CREATE TABLE IF NOT EXISTS `vehicle` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `brand_id` BIGINT NOT NULL COMMENT '品牌ID',
  `model` VARCHAR(100) NOT NULL COMMENT '车型',
  `guide_price` VARCHAR(50) COMMENT '指导价',
  `battery_capacity` VARCHAR(50) COMMENT '电池容量',
  `endurance` VARCHAR(50) COMMENT '续航',
  `fast_charge` VARCHAR(50) COMMENT '快充',
  `slow_charge` VARCHAR(50) COMMENT '慢充',
  `main_image` VARCHAR(255) COMMENT '主图',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  `is_featured` TINYINT DEFAULT 0 COMMENT '是否精选 0否 1是',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_brand_id` (`brand_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_is_featured` (`is_featured`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车辆表';

-- 车辆图片表
CREATE TABLE IF NOT EXISTS `vehicle_image` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `vehicle_id` BIGINT NOT NULL COMMENT '车辆ID',
  `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_vehicle_id` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车辆图片表';

-- 业务相关表
-- ============================================================

-- 旧车估价表
CREATE TABLE IF NOT EXISTS `vehicle_appraisal` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `old_brand` VARCHAR(100) NOT NULL COMMENT '旧车品牌',
  `intention_brand` VARCHAR(100) NOT NULL COMMENT '意向品牌',
  `purchase_year` VARCHAR(20) NOT NULL COMMENT '购入年份',
  `region` VARCHAR(100) NOT NULL COMMENT '所在地区',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态 PENDING未跟进 FOLLOWING已跟进 COMPLETED已完成',
  `follower_id` BIGINT COMMENT '跟进人ID',
  `follower_name` VARCHAR(50) COMMENT '跟进人姓名',
  `follow_time` DATETIME COMMENT '跟进时间',
  `remark` TEXT COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_phone` (`phone`),
  INDEX `idx_status` (`status`),
  INDEX `idx_follower_id` (`follower_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旧车估价表';

-- 新车询价表
CREATE TABLE IF NOT EXISTS `vehicle_inquiry` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `vehicle_id` BIGINT NOT NULL COMMENT '车辆ID',
  `brand_id` BIGINT NOT NULL COMMENT '品牌ID',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `need_exchange` TINYINT DEFAULT 0 COMMENT '是否需要置换 0否 1是',
  `dealer_name` VARCHAR(100) COMMENT '经销商名称',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态 PENDING待处理 CONTACTED已联系',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_vehicle_id` (`vehicle_id`),
  INDEX `idx_phone` (`phone`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新车询价表';

-- 活动表
CREATE TABLE IF NOT EXISTS `activity` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '活动名称',
  `type` VARCHAR(50) NOT NULL COMMENT '活动类型 LIMITED_OFFER限时优惠 EVENT活动中心',
  `image` VARCHAR(255) COMMENT '活动图片',
  `link_url` VARCHAR(500) COMMENT '跳转链接',
  `vehicle_id` BIGINT COMMENT '关联车辆ID（限时优惠用）',
  `content` TEXT COMMENT '活动详情',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  `start_time` DATETIME COMMENT '开始时间',
  `end_time` DATETIME COMMENT '结束时间',
  `sort_order` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_type` (`type`),
  INDEX `idx_status` (`status`),
  INDEX `idx_vehicle_id` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 权益表
CREATE TABLE IF NOT EXISTS `benefit` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '权益名称',
  `code` VARCHAR(50) NOT NULL UNIQUE COMMENT '权益编码',
  `type` VARCHAR(50) NOT NULL COMMENT '权益类型 PARTNER合作商 OWN自有 DEALER经销商',
  `image` VARCHAR(255) COMMENT '权益图片',
  `description` TEXT COMMENT '权益描述',
  `stock` INT DEFAULT 0 COMMENT '库存数量 -1表示无限',
  `status` TINYINT DEFAULT 1 COMMENT '状态 0下架 1上架',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_code` (`code`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权益表';

-- 用户权益领取记录表
CREATE TABLE IF NOT EXISTS `user_benefit` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `benefit_id` BIGINT NOT NULL COMMENT '权益ID',
  `benefit_name` VARCHAR(100) COMMENT '权益名称（冗余）',
  `benefit_image` VARCHAR(255) COMMENT '权益图片（冗余）',
  `quantity` INT DEFAULT 1 COMMENT '领取数量',
  `city` VARCHAR(50) COMMENT '行驶城市',
  `license_plate` VARCHAR(20) COMMENT '车牌号',
  `phone` VARCHAR(11) COMMENT '手机号',
  `verification_code` VARCHAR(10) COMMENT '验证码',
  `status` VARCHAR(20) DEFAULT 'UNUSED' COMMENT '使用状态 UNUSED未使用 USED已使用',
  `use_time` DATETIME COMMENT '使用时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_benefit_id` (`benefit_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户权益领取记录表';

-- 用户喜欢表
CREATE TABLE IF NOT EXISTS `user_favorite` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `vehicle_id` BIGINT NOT NULL COMMENT '车辆ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `uk_user_vehicle` (`user_id`, `vehicle_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_vehicle_id` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户喜欢表';

