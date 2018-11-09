-- 需要更新已付款成功到已完成的销售订单的可退金额
update `orders`
set `refund_amount` = IFNULL(`payable_amount`, '0.00')
where `status_id` between 2 and 5;
-- 需要更新已付款成功到已完成的销售订单明细条目可退金额和可退数量
update `order_detail`
inner join `orders` on `orders`.`id` = `order_detail`.`order_id`
set
`order_detail`.`refund_quantity` = IFNULL(`order_detail`.`quantity`, 0),
`order_detail`.`refund_amount` = IFNULL(`order_detail`.`real_amount`, '0.00')
where `orders`.`status_id` between 2 and 5;
