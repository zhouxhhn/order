/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.backend.index;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SalesShopResponse {

  @ApiModelProperty(value = "门店id")
  private Long id;
  /**
   * 门店code
   */
  @ApiModelProperty(value = "门店code")
  private String code;



  /**
   * 门店名称
   */
  @ApiModelProperty(value = "门店名称",required = true)
  private String name;


  /**
   * 门店sourceId
   */
  @ApiModelProperty(value = "门店sourceId")
  private String sourceId;


}
