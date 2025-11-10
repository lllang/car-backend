-- 数据库变更脚本 V2 - 更新询价和评估表
-- 执行时间: 2025-11-09

-- 1. 为新车询价表添加字段
ALTER TABLE `vehicle_inquiry`
ADD COLUMN `remark` TEXT COMMENT '备注' AFTER `status`,
ADD COLUMN `handler_id` BIGINT COMMENT '处理人ID' AFTER `remark`,
ADD COLUMN `handler_name` VARCHAR(50) COMMENT '处理人姓名' AFTER `handler_id`,
ADD COLUMN `handle_time` DATETIME COMMENT '处理时间' AFTER `handler_name`,
ADD COLUMN `brand_name` VARCHAR(100) COMMENT '品牌名称(冗余)' AFTER `brand_id`,
ADD COLUMN `vehicle_model` VARCHAR(200) COMMENT '车型(冗余)' AFTER `brand_name`,
ADD COLUMN `vehicle_price` DECIMAL(10,2) COMMENT '车辆价格(冗余)' AFTER `vehicle_model`,
ADD COLUMN `vehicle_image` VARCHAR(500) COMMENT '车辆图片(冗余)' AFTER `vehicle_price`,
ADD INDEX `idx_handler_id` (`handler_id`);

-- 2. 为旧车评估表添加COMPLETED状态支持（如果需要）
-- 状态字段已经支持COMPLETED，无需修改

-- 3. 数据迁移 - 从vehicle和brand表补充冗余数据到inquiry表
UPDATE vehicle_inquiry vi
LEFT JOIN vehicle v ON vi.vehicle_id = v.id
LEFT JOIN brand b ON vi.brand_id = b.id
SET 
  vi.brand_name = b.name,
  vi.vehicle_model = v.model,
  vi.vehicle_price = v.guide_price,
  vi.vehicle_image = v.main_image
WHERE vi.brand_name IS NULL;

-- 4. 添加注释说明
ALTER TABLE `vehicle_inquiry` COMMENT='新车询价表-包含车辆信息冗余';

