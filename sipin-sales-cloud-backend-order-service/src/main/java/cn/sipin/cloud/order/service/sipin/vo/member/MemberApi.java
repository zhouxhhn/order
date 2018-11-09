package cn.sipin.cloud.order.service.sipin.vo.member;



import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * 会员接口返回信息定义
 *
 * @author Sipin Backend Development Team
 */
@Data
public class MemberApi {

  /** 手机号码 */
  private String mobile;

  /** 会员昵称 */
  private String nickname;

  /** 会员地址 */
  @SerializedName("address")
  private MemberApiAddress[] addresses;

  /** 会员等级 */
  private MemberApiLevel level;

  /** 会员可用优惠券 */
  private MemberApiCoupon[] coupon;

  /** 积分 */
  private Integer point;
}
