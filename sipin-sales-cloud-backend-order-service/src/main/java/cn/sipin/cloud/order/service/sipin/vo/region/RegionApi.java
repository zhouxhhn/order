/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.vo.region;

import java.util.List;

import lombok.Data;

@Data
public class RegionApi {

  private int id;

  private int parentId;

  private String name;

  private int acceptable;

  private List<RegionApi> children;
}
