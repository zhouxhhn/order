package cn.sipin.cloud.order.service.sipin.vo.member;


import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 会员信息接口返回的等级信息
 *
 * @author Sipin Backend Development Team
 */
@Data
public class MemberApiLevel {

  /** 等级名称 */
  private String name;

  /** 等级所需积分 */
  @SerializedName("claim_point")
  private Integer claimPoint;

  /** 等级全局折扣率 */
  @SerializedName("discount_rate")
  private BigDecimal discountRate;

  /** 积分倍率 */
  @SerializedName("point_rate")
  private BigDecimal pointRate;
}
