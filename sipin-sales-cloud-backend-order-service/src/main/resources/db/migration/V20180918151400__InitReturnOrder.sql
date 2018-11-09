CREATE TABLE `return_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `no` varchar(45) DEFAULT NULL COMMENT '退货单号',
  `status_id` tinyint(4) NOT NULL COMMENT '状态',
  `refund_type` tinyint(4) NOT NULL COMMENT '退款方式',
  `refunded_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '退款总金额',
  `agency_code` varchar(64) NOT NULL COMMENT '经销商Code',
  `shop_code` varchar(64) NOT NULL COMMENT '店铺Code',
  `mobile` varchar(32) NOT NULL COMMENT '用户手机号 匿名用户时填Anonymous-User',
  `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
  `auditor_id` bigint(20) DEFAULT NULL COMMENT '审核者ID',
  `audited_at` datetime DEFAULT NULL COMMENT '审核时间',
  `reason_note` TEXT DEFAULT NULL COMMENT '退款原因',
  `admin_note` text COMMENT '管理员备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '软删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `finished_at` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `return_order_unique_no`(`no`),
  KEY `return_order_index_order_id`(`order_id`)
) COMMENT='销售退货单';


CREATE TABLE `return_order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `return_order_id` bigint(20) NOT NULL COMMENT '退货单ID',
  `order_detail_id` bigint(20) NOT NULL COMMENT '销售订单详情ID',
  `detail_no` varchar(45) NOT NULL COMMENT '退款详情号',
  `sku_no` varchar(50) NOT NULL COMMENT 'SKU NO',
  `refunded_quantity` int(11) NOT NULL DEFAULT '0' COMMENT '退货数量',
  `refunded_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '订单详情退款总金额',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '软删除',
  `remark` text COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `return_order_detail_unique_detail_no`(`detail_no`),
  KEY `return_order_detail_index_return_order_id`(`return_order_id`)
)COMMENT='销售退货单明细';