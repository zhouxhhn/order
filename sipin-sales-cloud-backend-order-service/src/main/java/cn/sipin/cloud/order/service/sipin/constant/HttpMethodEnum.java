package cn.sipin.cloud.order.service.sipin.constant;

/**
 * HTTP 请求方法枚举
 *
 * @author Sipin Backend Development Team
 */
public enum HttpMethodEnum {
  // POST 方法
  POST("POST"),
  // GET 方法
  GET("GET"),
  // PUT 方法
  PUT("PUT");

  private final String methodStr;

  HttpMethodEnum(String method) {
    this.methodStr = method;
  }

  public String method() {
    return methodStr;
  }
}
