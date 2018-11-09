package cn.sipin.cloud.order.service.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import java.util.List;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApi;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApiCoupon;
import cn.sipin.sales.cloud.order.pojo.OrderDetail;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.request.SalesOrderRequest;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.request.confirmPayment.ConfirmPaymentRequest;
import cn.sipin.sales.cloud.order.request.editNotes.EditNotesRequest;
import cn.sipin.sales.cloud.order.request.wholeDiscount.WholeDiscountRequest;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.ErpSalesOrderResponse;
import cn.sipin.sales.cloud.order.response.backend.index.IndexOrdersResponse;
import cn.siyue.platform.base.ResponseData;

/**
 * <p>
 * 销售订单 服务类
 * </p>
 *
 */
public interface OrdersServiceContract extends IService<Orders> {

  Orders setAndGetOwnOrders(String orderNo);

  Orders create(
      SalesOrderRequest salesOrderRequest, AgencyCodeResponse agencyInfoVo, MemberApi member,
      List<OrderDetail> orderDetailList,
      MemberApiCoupon coupon
  );

  Orders selectByNo(String orderNo);

  /**
   * 后台获取销售订单列表
   */
  ResponseData<Page<IndexOrdersResponse>> backendIndex(int page, int size, IndexOrdersRequest request);


  /**
   * 后台获取销售订单详情数据
   */
  ResponseData backendDetail(String no);


  /**
   * 前台整单优惠
   */
  ResponseData wholeDiscount(String no,WholeDiscountRequest request);


  Page<ErpSalesOrderResponse> selectOrderForErpPage(Page<ErpSalesOrderResponse> orderVoPage, ErpOrdersRequest request);

  /**
   * 确认收银
   */
  ResponseData confirmPayment(String no,ConfirmPaymentRequest request);

  /**
   * 备注
   */
  ResponseData editNotes(String no,EditNotesRequest request);

  /**
   * 取消订单
   */
  ResponseData cancelOrder(String no);

  /**
   * 确认完成
   */
  ResponseData confirmComplete(String no);

  /**
   * 得到支付交易码
   */
  ResponseData getPaymentCode(String no);

  /**
   * 后台导出excel表单
   */
  ResponseData exportExcel(IndexOrdersRequest request);


}
