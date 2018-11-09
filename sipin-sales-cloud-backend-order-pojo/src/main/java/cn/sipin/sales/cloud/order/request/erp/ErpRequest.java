/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.erp;

import java.util.Map;

public abstract class ErpRequest {

  public abstract Map<String, String> toParams();

}
