package cn.sipin.cloud.order.service.sipin.vo.member;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * 会员地址信息
 *
 * @author Sipin Backend Development Team
 */
@Data
public class MemberApiAddress {

  /**
   * 地址ID，用于订单提交
   */
  @SerializedName("address_id")
  private Integer addressId;

  /**
   * 省
   */
  private String province;

  /**
   * 市
   */
  private String city;

  /**
   * 区
   */
  private String district;

  /**
   * 收货地址
   */
  private String address;

  /**
   * 收货人
   */
  private String consignee;

  /**
   * 收货人手机
   */
  private String mobile;
}
