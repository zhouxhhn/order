package cn.sipin.cloud.order.service.sipin.constant;

/**
 * HTTP 常量定义
 *
 * @author Sipin Backend Development Team
 */
public class HttpConstant {

  /** 请求Header Accept */
  public static final String HTTP_HEADER_ACCEPT = "Accept";

  /** 请求Body内容MD5 Header */
  public static final String HTTP_HEADER_CONTENT_MD5 = "Content-MD5";

  /** 请求Header Content-Type */
  public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

  /** 请求Header Date */
  public static final String HTTP_HEADER_DATE = "Date";

  /** 店铺 */
  public static final String X_APP_STORE = "X-App-Store";

  /** 操作人 */
  public static final String X_APP_ACTOR = "X-App-Actor";

  /** 签名Header */
  public static final String X_CA_SIGNATURE = "X-Ca-Signature";

  /** 所有参与签名的Header */
  public static final String X_CA_SIGNATURE_HEADERS = "X-Ca-Signature-Headers";

  /** 请求时间戳 */
  public static final String X_CA_TIMESTAMP = "X-Ca-Timestamp";

  /** 请求放重放Nonce,15分钟内保持唯一,建议使用UUID */
  public static final String X_CA_NONCE = "X-Ca-Nonce";

  /** APP KEY */
  public static final String X_CA_KEY = "X-Ca-Key";

  /** API 环境 */
  public static final String X_CA_STAGE = "X-Ca-Stage";

  /** JSON类型Content-Type */
  public static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";

  /** 构造签名时各部分的连接符 */
  public static final String HEADER_LF = "\n";
}
