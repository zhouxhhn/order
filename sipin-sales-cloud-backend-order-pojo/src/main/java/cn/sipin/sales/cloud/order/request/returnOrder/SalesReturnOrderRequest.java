/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.returnOrder;

import java.util.List;

import javax.validation.constraints.NotNull;

import cn.sipin.sales.cloud.order.request.vo.ReturnSkuDetailVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SalesReturnOrderRequest")
public class SalesReturnOrderRequest {

  @ApiModelProperty(value = "销售订单号")
  private String orderNo;

  @NotNull(message = "退货单商品详情")
  @ApiModelProperty(value = "退货单商品详情")
  private List<ReturnSkuDetailVo> returnSkuDetails;

  @NotNull(message = "退款方式不能为空")
  @ApiModelProperty(value = "退款方式")
  private Byte returnType;

  @ApiModelProperty(value = "退款理由")
  private String reasonNote;

}
