/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.response;

import java.util.List;

import cn.sipin.cloud.order.service.sipin.vo.region.RegionApi;
import lombok.Data;

@Data
public class RegionApiResponse extends AbstractApiResponse {

  private List<RegionApi> data;
}
