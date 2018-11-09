package cn.sipin.cloud.order.service.sipin.vo.logistics;

import java.util.Date;

import lombok.Data;

/**
 * 物流历史信息
 *
 * @author Sipin Backend Development Team
 */
@Data
public class LogisticsApiHistory {

  private Date time;

  private Date ftime;

  private String context;
}
