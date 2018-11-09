/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.front.index;

import java.util.List;
import lombok.Data;

@Data
public class SalesUserResponse {

  private String total;

  private String size;

  private String pages;

  private String current;

  private List<SalesUser> records;

}
