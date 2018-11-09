/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response;

import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MaterialResponse {

  /**
   * 物料编码
   */
  @JsonProperty("number")
  private String skuNo;

  /**
   * 物料名称
   */
  @JsonProperty("name")
  private String name;

  /**
   * 物料规格
   */
  @JsonProperty("specification")
  private String specification;

  /**
   * 物料材质
   */
  @JsonProperty("texture")
  private String texture;

  /**
   * 物料颜色
   */
  @JsonProperty("color")
  private String color;

  /**
   * SPU
   */
  @JsonProperty("spu")
  private String spu;

  /**
   * SKU
   */
  @JsonProperty("sku")
  @SerializedName("sku")
  private String skuSn;

  /**
   * 是否启用
   */
  @JsonProperty("status")
  private Integer status;

  /**
   * 禁用状态：1为未禁用，0为已禁用
   */
  @JsonProperty("forbidStatus")
  private Integer forbidStatus;

  /**
   * 图片资源
   */
  @JsonProperty("imgPath")
  private String imgPath;

  /**
   * 售价
   */
  @JsonProperty("amount")
  private BigDecimal amount;

  /**
   * 库存数
   */
  @JsonProperty("stockQty")
  private Integer stockQty;

}
