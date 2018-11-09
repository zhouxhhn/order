/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.backend.index;

import com.baomidou.mybatisplus.annotations.TableField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SalesAgencyIndexResponse {

  @ApiModelProperty(value = "经销商id")
  private Long id;

  @ApiModelProperty(value = "经销商帐号")
  private String code;

  @ApiModelProperty(value = "经销商名称")
  private String name;

  private List<SalesShopResponse> shopResponseList =new ArrayList<>();
}
