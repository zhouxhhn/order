ALTER TABLE `orders`
ADD COLUMN `refund_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '订单可退金额' AFTER `paid_amount`;

ALTER TABLE `order_detail`
ADD COLUMN `refund_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00  COMMENT '订单详情可退金额' AFTER `discount_amount`,
ADD COLUMN `refund_quantity` INT(11) NOT NULL DEFAULT 0  COMMENT '订单可退数' AFTER `refund_amount`;


