package cn.sipin.cloud.order.service.sipin.vo.coupon;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

import cn.sipin.cloud.order.service.sipin.constant.CouponDiscountTypeEnum;
import cn.sipin.cloud.order.service.sipin.constant.CouponScopeEnum;
import lombok.Data;

/**
 * 优惠券适用规则响应实体
 *
 * @author Sipin Backend Development Team
 */
@Data
public class CouponApiTask {

  /**
   * 优惠券名称
   */
  private String name;

  /**
   * 优惠券面值，可为金额(500.00)或折扣(0.75)
   */
  private BigDecimal value;

  /**
   * 适用范围
   */
  private CouponScopeEnum scope;

  /**
   * 是否是无门槛的： 0: 无门槛 1: 满 X 元使用
   */
  private Byte requirement;

  /**
   * 金额门槛
   */
  private BigDecimal threshold;

  /**
   * 优惠类型
   */

  @SerializedName("discount_type")
  private CouponDiscountTypeEnum discountType;

  /**
   * 优惠券限制， 取值范围： 6：斯品空间-广州 7：斯品空间-上海 8：斯品空间-珠海
   */
  private Integer[] limit;

  /**
   * 优惠券限制名， 取值范围： 6：斯品空间-广州 7：斯品空间-上海 8：斯品空间-珠海
   */
  @SerializedName("limit_name")
  private String[] limitName;

  /**
   * 有效使用开始时间
   */
  @SerializedName("valid_time_start_at")
  private String validTimeStartAt;

  /**
   * 有效使用结束时间
   */
  @SerializedName("valid_time_end_at")
  private String validTimeEndAt;

  /**
   * 指定 sku
   */
  @SerializedName("goods_sku_sn")
  private String[] goodsSkuSn;

  /**
   * 由经销商-商品微服务提供
   */
  private String[] goodsSkuNo;

  /**
   * 使用须知
   */
  private String summary;
}
