/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.response;

import cn.sipin.cloud.order.service.sipin.vo.point.UpdatePointApi;
import lombok.Data;

@Data
public class UpdatePointApiResponse extends  AbstractApiResponse{

  private UpdatePointApi data;
}
