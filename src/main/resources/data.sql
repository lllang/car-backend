-- 初始化数据

-- 插入角色数据
INSERT INTO `role` (`id`, `name`, `code`, `description`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限'),
(2, '普通管理员', 'ADMIN', '除权限管理外的其他功能'),
(3, '业务员', 'SALESMAN', '只能查看和跟进业务数据')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 插入默认超级管理员账号 (密码: admin123)
-- 密码使用 BCrypt 加密，这里使用明文的 BCrypt hash
INSERT INTO `admin` (`id`, `username`, `password`, `real_name`, `phone`, `role_id`, `status`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', '13800138000', 1, 1)
ON DUPLICATE KEY UPDATE username=VALUES(username);

-- 插入权限数据（页面级别权限）
INSERT INTO `permission` (`id`, `name`, `code`, `type`, `parent_id`, `path`) VALUES
-- 一级菜单
(1, '权限管理', 'permission:manage', 'MENU', 0, '/admin/permission'),
(2, '品牌管理', 'brand:manage', 'MENU', 0, '/admin/brand'),
(3, '车辆管理', 'vehicle:manage', 'MENU', 0, '/admin/vehicle'),
(4, '旧车估价', 'appraisal:manage', 'MENU', 0, '/admin/appraisal'),
(5, '新车询价', 'inquiry:manage', 'MENU', 0, '/admin/inquiry'),
(6, '活动管理', 'activity:manage', 'MENU', 0, '/admin/activity'),
(7, '权益管理', 'benefit:manage', 'MENU', 0, '/admin/benefit'),
(8, '用户管理', 'user:manage', 'MENU', 0, '/admin/user'),

-- 权限管理子权限
(11, '账号列表', 'permission:account:list', 'BUTTON', 1, NULL),
(12, '创建账号', 'permission:account:create', 'BUTTON', 1, NULL),
(13, '编辑账号', 'permission:account:update', 'BUTTON', 1, NULL),
(14, '删除账号', 'permission:account:delete', 'BUTTON', 1, NULL),

-- 品牌管理子权限
(21, '品牌列表', 'brand:list', 'BUTTON', 2, NULL),
(22, '创建品牌', 'brand:create', 'BUTTON', 2, NULL),
(23, '编辑品牌', 'brand:update', 'BUTTON', 2, NULL),
(24, '删除品牌', 'brand:delete', 'BUTTON', 2, NULL),

-- 车辆管理子权限
(31, '车辆列表', 'vehicle:list', 'BUTTON', 3, NULL),
(32, '创建车辆', 'vehicle:create', 'BUTTON', 3, NULL),
(33, '编辑车辆', 'vehicle:update', 'BUTTON', 3, NULL),
(34, '删除车辆', 'vehicle:delete', 'BUTTON', 3, NULL),

-- 估价管理子权限
(41, '估价列表', 'appraisal:list', 'BUTTON', 4, NULL),
(42, '跟进估价', 'appraisal:follow', 'BUTTON', 4, NULL),

-- 询价管理子权限
(51, '询价列表', 'inquiry:list', 'BUTTON', 5, NULL),
(52, '更新状态', 'inquiry:update', 'BUTTON', 5, NULL),

-- 活动管理子权限
(61, '活动列表', 'activity:list', 'BUTTON', 6, NULL),
(62, '创建活动', 'activity:create', 'BUTTON', 6, NULL),
(63, '编辑活动', 'activity:update', 'BUTTON', 6, NULL),
(64, '删除活动', 'activity:delete', 'BUTTON', 6, NULL),

-- 权益管理子权限
(71, '权益列表', 'benefit:list', 'BUTTON', 7, NULL),
(72, '创建权益', 'benefit:create', 'BUTTON', 7, NULL),
(73, '编辑权益', 'benefit:update', 'BUTTON', 7, NULL),
(74, '删除权益', 'benefit:delete', 'BUTTON', 7, NULL),

-- 用户管理子权限
(81, '用户列表', 'user:list', 'BUTTON', 8, NULL),
(82, '用户详情', 'user:view', 'BUTTON', 8, NULL)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 为超级管理员分配所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `permission`
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- 为普通管理员分配权限（除权限管理外）
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 2, id FROM `permission` WHERE parent_id != 1 AND id != 1
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

-- 为业务员分配权限（只能查看和跟进）
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES
(3, 4), (3, 41), (3, 42),  -- 估价管理
(3, 5), (3, 51), (3, 52)   -- 询价管理
ON DUPLICATE KEY UPDATE role_id=VALUES(role_id);

