ALTER TABLE `orders`
ADD COLUMN `give_point` INT(11) NOT NULL DEFAULT 0 COMMENT '完成订单-赠送积分' AFTER `used_point`,
ADD COLUMN `return_point` INT(11) NOT NULL DEFAULT 0 COMMENT '退货退款-已退积分' AFTER `give_point`;

ALTER TABLE `return_order`
ADD COLUMN `is_return_point` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '是否已退积分' AFTER `admin_note`,
ADD COLUMN `return_point` INT(11) NOT NULL DEFAULT 0 COMMENT '已退积分数' AFTER `is_return_point`;
