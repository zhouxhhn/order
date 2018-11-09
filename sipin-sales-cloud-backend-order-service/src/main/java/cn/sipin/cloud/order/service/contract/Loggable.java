/*
 * (C) Copyright 2018 Siyue Holding Group.
 */

package cn.sipin.cloud.order.service.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供日志记录能力的接口，使用接口默认方法实现。
 *
 * @author Sipin ERP Development Team
 */
public interface Loggable {

  /**
   * 日志记录
   *
   * @return Logger
   */
  default Logger logger() {
    return LoggerFactory.getLogger(getClass());
  }
}
