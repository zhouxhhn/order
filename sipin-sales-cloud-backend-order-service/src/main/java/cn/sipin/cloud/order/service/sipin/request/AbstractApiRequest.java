package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * API 请求抽象类
 *
 * @author Sipin Backend Development Team
 */

public abstract class AbstractApiRequest<T> {

  /**
   * 构造 OkHttp Request
   *
   * @return Request
   */
  public Request build(String token, String host, String ip) {

    Request.Builder requestBulder = new Request.Builder()
        .addHeader("X-Sipin-Sales-Token", token)
        .addHeader("Host", host)
        .addHeader("Origin", ip)
        .url(url(ip));
    switch (method()) {
      case POST: {

        return requestBulder.post(requestBody()).build();
      }
      case PUT: {
        return requestBulder.put(requestBody()).build();
      }
      case GET: {
        return requestBulder.build();
      }
      default: {
        throw new IllegalArgumentException("不支持的请求方法");
      }
    }
  }

  /**
   * API 请求路径
   *
   * @return String
   */
  protected abstract String path();

  /**
   * API 请求地址
   *
   * @return String
   */
  private String url(String ip) {
    return ip + path();
  }

  /**
   * API 请求方法
   *
   * @return HttpMethodEnum
   */
  protected abstract HttpMethod method();

  /**
   * POST 或 PUT 请求时提交的数据
   *
   * @return String
   */
  protected abstract String body();

  /**
   * 请求数据
   *
   * @return RequestBody
   */
  private RequestBody requestBody() {
    if (body() == null) {
      return RequestBody.create(MediaType.parse("application/json"), "");
    }
    return RequestBody.create(MediaType.parse("application/json"), body());
  }

  /**
   * 此请求对应的响应类
   *
   * @return Class<T>
   */
  public abstract Class<T> responseClass();
}


