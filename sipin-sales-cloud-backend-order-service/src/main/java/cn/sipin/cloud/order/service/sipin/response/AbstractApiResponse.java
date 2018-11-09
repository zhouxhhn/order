package cn.sipin.cloud.order.service.sipin.response;

import lombok.Data;

/**
 * API 响应抽象类
 *
 */
@Data
public abstract class AbstractApiResponse {

  /** 业务返回码 */
  protected Integer code = 0;

  /** 返回消息 */
  protected String msg;
}
