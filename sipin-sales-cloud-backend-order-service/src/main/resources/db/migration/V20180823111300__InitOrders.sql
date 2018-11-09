drop TABLE  if exists orders;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `no` varchar(45) NOT NULL COMMENT '销售订单号',
  `status_id` int(11) NOT NULL DEFAULT '0' COMMENT '状态ID',
  `amount` decimal(12,2) DEFAULT NULL COMMENT '订单金额',
  `original_payable_amount` decimal(12,2) DEFAULT NULL COMMENT '原应收金额',
  `payable_amount` decimal(12,2) DEFAULT NULL COMMENT '实际应收金额',
  `paid_amount` decimal(12,2) DEFAULT NULL COMMENT '已收金额',
  `agency_code` varchar(64) NOT NULL COMMENT '经销商Code',
  `shop_code` varchar(64) NOT NULL COMMENT '店铺Code',
  `mobile` varchar(32) NOT NULL COMMENT '用户手机号 匿名用户时填Anonymous-User',
  `discount` decimal(12,2) DEFAULT NULL COMMENT '当前会员折扣',
  `used_point` int(11) NOT NULL DEFAULT '0' COMMENT '已使用积分数',
  `saler_id` bigint(20) DEFAULT NULL COMMENT '门店导购员ID',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '操作员ID',
  `note` text COMMENT '客户/销售员备注',
  `admin_note` text COMMENT '管理员备注',
  `canceled_at` datetime DEFAULT NULL COMMENT '取消时间',
  `paid_at` datetime DEFAULT NULL COMMENT '付款时间',
  `finished_at` datetime DEFAULT NULL COMMENT '完成时间',
  `is_pickup` tinyint(4) DEFAULT NULL COMMENT '是否为自提单',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '软删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `orders_unique_no` (`no`),
  KEY `orders_index_mobile` (`mobile`)
) COMMENT='销售订单';

drop TABLE  if exists order_detail;
CREATE TABLE `order_detail` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `detail_no` varchar(45) NOT NULL COMMENT '订单商品交易号',
  `sku_sn` varchar(50) NOT NULL DEFAULT '' COMMENT 'SKU',
  `sku_no` varchar(50) NOT NULL DEFAULT '' COMMENT '产品SKU编号',
  `name` varchar(150) NOT NULL DEFAULT '' COMMENT '商品名称',
  `specification` varchar(150) NOT NULL DEFAULT '' COMMENT '物料规格',
  `texture` varchar(150) NOT NULL DEFAULT '' COMMENT '物料材质',
  `color` varchar(50) NOT NULL DEFAULT '' COMMENT '物料颜色',
  `img_path` varchar(150) DEFAULT NULL COMMENT '图片地址',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  `original_amount` decimal(12,2) DEFAULT NULL COMMENT '原价',
  `amount` decimal(12,2) DEFAULT NULL COMMENT '现价',
  `discount_amount` decimal(12,2) DEFAULT NULL COMMENT '折后价',
  `real_amount` decimal(12,2) DEFAULT NULL COMMENT '实际应收金额',
  `real_single_amount` decimal(12,2) DEFAULT NULL COMMENT '实际应收单价',
  `subtotal` decimal(12,2) DEFAULT NULL COMMENT '小计',
  `is_pickup` tinyint(1) DEFAULT '0' COMMENT '是否自提',
  `is_sample` tinyint(1) DEFAULT '0' COMMENT '是否样品',
  `is_gift` tinyint(1) DEFAULT '0' COMMENT '是否赠品',
  `note` text COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE key `order_detail_unique_detail_no` (`detail_no`),
  KEY `order_detail_index_order_id` (`order_id`),
  KEY `order_detail_index_sku_no` (`sku_no`)
) COMMENT='订单明细';

drop TABLE  if exists order_consignee;
CREATE TABLE `order_consignee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `name` varchar(64) NOT NULL COMMENT '收货人',
  `mobile` varchar(32) NOT NULL COMMENT '收货人手机',
  `phone` varchar(32) DEFAULT NULL COMMENT '收货人电话',
  `province` varchar(50) NOT NULL COMMENT '省',
  `city` varchar(50) NOT NULL COMMENT '市',
  `district` varchar(50) NOT NULL COMMENT '区',
  `addr` varchar(128) NOT NULL COMMENT '详细地址',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_consignee_index_order_id` (`order_id`)
) COMMENT='订单收货人';

drop TABLE  if exists order_discount;
CREATE TABLE `order_discount` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '优惠折扣类型，0：优惠券-满减 1：优惠券-满折 2: 积分折算金额 3: 整单优惠',
  `order_id` bigint(20) NOT NULL COMMENT '优惠券作用的订单号',
  `code` varchar(64) NULL COMMENT '优惠券',
  `discount_value` decimal(12,2) NOT NULL COMMENT '优惠券面值/折扣',
  PRIMARY KEY (`id`),
  KEY `order_coupon_index_order_id` (`order_id`)
) COMMENT='订单优惠折扣';

drop TABLE  if exists payment;
CREATE TABLE `payment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `cashier_id` bigint(20) DEFAULT NULL COMMENT '门店收银员ID',
  `payment_no` varchar(64) NOT NULL COMMENT '支付流水号',
  `paid_at` datetime DEFAULT NULL COMMENT '支付时间',
  `status_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付状态',
  `price` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额',
  `give_change` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '找零',
  `real_receive_price` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '实收金额',
  `partner_id` varchar(15) DEFAULT NULL COMMENT '用户所使用用卡组织/第三方支付',
  `partner_trade_no` varchar(100) DEFAULT NULL COMMENT '第三方交易号',
  `merchant_number` varchar(100) DEFAULT NULL COMMENT '商户号',
  `merchant_name` varchar(100) DEFAULT NULL COMMENT '商户名',
  `account` varchar(100) DEFAULT NULL COMMENT '用户交易账号',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '软删除',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `payment_unique_payment_no` (`payment_no`),
  KEY `order_id` (`order_id`)
) COMMENT='支付流水信息表';

drop TABLE  if exists order_sync;
CREATE TABLE `order_sync` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`order_id` bigint(20) NOT NULL COMMENT '订单ID',
`type_id` tinyint(4) NOT NULL DEFAULT '0' COMMENT '同步类型 0：核销积分，1：增加积分，2：核销卡券',
`sync_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单同步状态',
`sync_content` TEXT COMMENT '同步内容',
`created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`),
UNIQUE KEY `order_sync_unique_order_id_type_id` (`order_id`,`type_id`)
) COMMENT='订单同步表';

drop TABLE  if exists order_express;
CREATE TABLE `order_express` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(45) NOT NULL COMMENT '订单号',
  `express_no` varchar(128) NOT NULL COMMENT '运单号',
  `express_company` varchar(64) DEFAULT NULL COMMENT 'K3物流公司名称',
  `express_company_code` varchar(25) DEFAULT NULL COMMENT 'K3物流公司code',
  `note` text NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_express_unique_order_express_no` (`order_no`,`express_no`)
) COMMENT='订单快递物流表';