package cn.sipin.sales.cloud.order.response.returnOrder;

import cn.sipin.sales.cloud.order.common.ReturnOrderCommonVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ReturnOrderVo extends ReturnOrderCommonVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 外部编码 K3客户编码
     */
    @ApiModelProperty(value = "外部编码")
    private String outerCode;

    /**
     * sourceId 对应斯品商城 create source Id
     */
    @ApiModelProperty(value = "sourceId")
    private String sourceId;


    private List<ReturnOrderDetailSearchVo> detailList;

}
