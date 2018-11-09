package cn.sipin.cloud.order.service.service;

import cn.sipin.sales.cloud.order.common.PageResponse;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.pojo.ReturnOrder;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.request.returnOrder.SalesReturnOrderRequest;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderVo;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.siyue.platform.base.ResponseData;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 销售退货单 服务类
 * </p>
 *
 */
public interface ReturnOrderServiceContract extends IService<ReturnOrder> {

  ReturnOrder setAndGetOwnReturnOrder(String returnOrderNo);

  ReturnOrder create(Orders orders, SalesReturnOrderRequest salesReturnOrderRequest, AgencyCodeResponse agencyCodeResponse);

  ReturnOrder selectByNo(String no);

  Boolean audit(ReturnOrder returnOrder);

  Page<IndexReturnOrdersResponse> selectReturnOrderPage(ReturnOrder returnOrder, Page<IndexReturnOrdersResponse> orderVoPage, IndexReturnOrdersRequest request,boolean flag);

  public ResponseData<PageResponse<ReturnOrderVo>> search(ReturnOrderSearchRequest requestParam);
}
