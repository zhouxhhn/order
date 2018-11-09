/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.client;

import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Loggable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.sipin.cloud.order.service.sipin.config.SipinProperties;
import cn.sipin.cloud.order.service.sipin.request.AbstractApiRequest;
import cn.sipin.cloud.order.service.util.JsonTransformer;
import cn.siyue.platform.exceptions.exception.RequestException;
import cn.siyue.platform.httplog.aspect.LogAspect;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class SipinClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

  private SipinProperties sipinProperties;

  /**
   * OkHttpClient
   */
  private OkHttpClient client;

  @Autowired
  public SipinClient(SipinProperties sipinProperties) {
    this.sipinProperties = sipinProperties;

    this.client =
        new OkHttpClient.Builder()
            .addInterceptor(
                new CurlInterceptor(
                    new Loggable() {
                      @Override
                      public void log(String s) {
                        LOGGER.info(s);
                      }
                    }))
            .connectTimeout(sipinProperties.getClient().getConnectTimeout(), TimeUnit.SECONDS)
            .writeTimeout(sipinProperties.getClient().getWriteTimeout(),TimeUnit.SECONDS)
            .readTimeout(sipinProperties.getClient().getReadTimeout(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
    // 最大并发请求数
    this.client.dispatcher().setMaxRequests(64);
    // 最大请求的host的请求数
    this.client.dispatcher().setMaxRequestsPerHost(64);
  }

  /**
   * 执行 API 请求
   *
   * @param request AbstractApiRequest<T>
   * @return T
   * @throws RequestException 请求异常
   */
  public <T> T execute(AbstractApiRequest<T> request) throws RequestException {

    Request httpReq = request.build(sipinProperties.getToken(), sipinProperties.getHost(), sipinProperties.getIp());
    try {
      Response response = client.newCall(httpReq).execute();
      // 以下为响应检查，只有发生 HTTP 错误时才会抛异常
      // 业务异常不在此列，仍会正常返回
      if (response == null) {
        throw new RequestException(400, "API 请求无响应！");
      }
      if (response.body() == null) {
        throw new RequestException(400, "API 请求响应为空！");
      }


      String respStr = response.body().string();
      LOGGER.info(respStr);
      return JsonTransformer.toObject(respStr, request.responseClass());
    } catch (IOException e) {
      throw new RequestException(500, e.getLocalizedMessage());
    }
  }
}

