package cn.sipin.cloud.order.service.sipin.vo.logistics;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * 订单物流信息返回数组中的一项
 *
 * @author Sipin Backend Development Team
 */
@Data
public class LogisticsApiData {

  @SerializedName("order_no")
  private String orderNo;

  @SerializedName("express_no")
  private String expressNo;

  private String partner;

  private String current;

  private LogisticsApiHistory[] history;
}
