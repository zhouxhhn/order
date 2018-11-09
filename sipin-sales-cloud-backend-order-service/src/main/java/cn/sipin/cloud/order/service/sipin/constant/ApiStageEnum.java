package cn.sipin.cloud.order.service.sipin.constant;

/**
 * 接口环境定义
 *
 * @author Sipin Backend Development Team
 */
public enum ApiStageEnum {
  // 测试环境
  TEST("TEST"),
  // 预发布
  PRE("PRE"),
  // 正式环境
  RELEASE("RELEASE");

  private String stage;

  ApiStageEnum(String stage) {
    this.stage = stage;
  }

  public String stage() {
    return this.stage;
  }
}
