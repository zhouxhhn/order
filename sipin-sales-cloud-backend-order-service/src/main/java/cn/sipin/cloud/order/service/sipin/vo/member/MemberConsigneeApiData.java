/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.vo.member;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MemberConsigneeApiData {

  @SerializedName("province_id")
  private Integer provinceId;

  @SerializedName("province_name")
  private String provinceName;

  @SerializedName("city_id")
  private Integer cityId;

  @SerializedName("city_name")
  private String cityName;

  @SerializedName("district_id")
  private Integer districtId;

  @SerializedName("district_name")
  private String districtName;

  @SerializedName("address")
  private String address;

  @SerializedName("consignee")
  private String consignee;

  @SerializedName("mobile")
  private String mobile;

  @SerializedName("is_default")
  private Byte isDefault;

}
