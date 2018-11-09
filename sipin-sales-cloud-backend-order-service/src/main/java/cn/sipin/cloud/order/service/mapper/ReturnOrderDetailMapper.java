package cn.sipin.cloud.order.service.mapper;

import cn.sipin.sales.cloud.order.dto.returnorderdetail.ReturnOrderDetailDto;
import cn.sipin.sales.cloud.order.pojo.ReturnOrderDetail;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 销售退货单明细 Mapper 接口
 * </p>
 *
 */
@Repository
public interface ReturnOrderDetailMapper extends BaseMapper<ReturnOrderDetail> {

    public List<ReturnOrderDetailDto> getListByReturnOrderIds(@Param("idList") List<Long> idList);
}
