package cn.sipin.cloud.order.service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;

/**
 * <p>
 * 销售订单 Mapper 接口
 * </p>
 *
 */
public interface OrdersMapper extends BaseMapper<Orders> {

  List<Orders> backendIndex(@Param("limit") Integer limit, @Param("offset") Integer offset,
                            @Param("createdAtBefore") String createdAtBefore,
                            @Param("createdAtAfter") String createdAtAfter,@Param("shopCode") String shopCode,
                            @Param("request") IndexOrdersRequest request);

  /**
   * 后台获取销售订单详情数据
   */
  Orders backendDetail(@Param("no") String no);

  List<Orders> selectOrderForErpPage(@Param("limit") int limit, @Param("offset") int offset, @Param("request") ErpOrdersRequest request);

  Integer selectOrderForErpPageCount(@Param("request") ErpOrdersRequest request);

  Orders selectByNo(@Param("orderNo") String orderNo);
}
