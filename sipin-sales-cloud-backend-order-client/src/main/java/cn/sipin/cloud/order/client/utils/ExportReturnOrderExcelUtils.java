/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import cn.sipin.sales.cloud.order.constants.AuditStatus;
import cn.sipin.sales.cloud.order.constants.OrderDiscountTypeId;
import cn.sipin.sales.cloud.order.constants.RefundType;
import cn.sipin.sales.cloud.order.response.SalesOrderDetailResponse;
import cn.sipin.sales.cloud.order.response.SalesOrderDiscountResponse;
import cn.sipin.sales.cloud.order.response.backend.index.IndexOrdersResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.sipin.sales.cloud.order.response.vo.ReturnOrderDetailVo;

public class ExportReturnOrderExcelUtils {


  public static void export(HttpServletResponse res, List<IndexReturnOrdersResponse> ordersList) throws Exception{
    String[] orderTitles = { "销售退货单号","销售订单号", "门店", "经销商",  "下单时间", "审核时间", "订单状态", "操作员","退款金额", "退款原因",
                             "退款方式"};
    String[] orderDetailTitles = {"商品交易号", "商品名称","商品编号", "属性规格", "退货数量", "退款金额"};

    ServletOutputStream out=res.getOutputStream();
    String fileName=new String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), "UTF-8");
    res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    res.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");

    // 1.创建一个workbook，对应一个Excel文件
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 2.在webbook中添加一个sheet,对应Excel文件中的sheet
    HSSFSheet hssfSheet = workbook.createSheet("销售退货单");
    // 3.在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
    HSSFRow hssfRow = hssfSheet.createRow(0);
    // 4.创建单元格，并设置值表头 设置表头居中
    HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
    //居中样式
    hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

    HSSFCell hssfCell = null;
    int length = orderTitles.length;
    for (int i = 0; i < length; i++) {
      hssfCell = hssfRow.createCell(i);//列索引从0开始
      hssfCell.setCellValue(orderTitles[i]);//列名1
      hssfCell.setCellStyle(hssfCellStyle);//列居中显示
    }

    for (int i = 0; i < orderDetailTitles.length; i++) {
      hssfCell = hssfRow.createCell(length+i);
      hssfCell.setCellValue(orderDetailTitles[i]);//列名1
      hssfCell.setCellStyle(hssfCellStyle);//列居中显示
    }

    // 5.写入实体数据
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    int total = 1;

    if (ordersList != null && ordersList.size() > 0) {
      for (int i = 0; i < ordersList.size(); i++) {
        hssfRow = hssfSheet.createRow(total);
        IndexReturnOrdersResponse order = ordersList.get(i);
        // 6.创建单元格，并设置值

        //销售退货单号
        String no = "";
        if (order.getNo() != null) {
          no = order.getNo() + "";
        }
        hssfRow.createCell(0).setCellValue(no);

        //销售订单号
        String salesOrderNo = "";
        if (order.getSalesOrderNo() != null) {
          salesOrderNo = order.getSalesOrderNo() + "";
        }
        hssfRow.createCell(1).setCellValue(salesOrderNo);

        //门店
        String shopName = "";
        if (order.getShopName() != null) {
          shopName = order.getShopName();
        }
        hssfRow.createCell(2).setCellValue(shopName);

        //经销商
        String agencyName = "";
        if (order.getAgencyName() != null) {
          agencyName = order.getAgencyName();
        }
        hssfRow.createCell(3).setCellValue(agencyName);


        //下单时间
        String createdAt = "";
        if (order.getCreatedAt() != null) {
          createdAt = order.getCreatedAt().format(formatter);
        }
        hssfRow.createCell(4).setCellValue(createdAt);

        //审核时间
        String auditedAt = "";
        if (order.getAuditedAt() != null) {
          auditedAt = order.getAuditedAt().format(formatter);
        }
        hssfRow.createCell(5).setCellValue(auditedAt);

        //订单状态
        String status = "";
        if (order.getStatusId() != null) {
          status = AuditStatus.getDescriptionByValue(order.getStatusId());
        }
        hssfRow.createCell(6).setCellValue(status);

        //操作员
        String creatorName = "";
        if (order.getCreatorName() != null) {
          creatorName = order.getCreatorName();
        }
        hssfRow.createCell(7).setCellValue(creatorName);

        //退款金额
        BigDecimal refundedAmount = new BigDecimal(0);
        if (order.getRefundedAmount() != null) {
          refundedAmount = order.getRefundedAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(8).setCellValue(refundedAmount.toString());

        //退款原因
        String reasonNote = "";
        if (order.getReasonNote() != null) {
          reasonNote = order.getReasonNote();
        }
        hssfRow.createCell(9).setCellValue(reasonNote);

        //退款方式
        String refundType = "";
        if (order.getRefundType() != null) {
          refundType = RefundType.getDescriptionByValue(order.getRefundType());
        }
        hssfRow.createCell(10).setCellValue(refundType);

        //订单金额
        if(order.getReturnOrderDetailVos() != null && !order.getReturnOrderDetailVos().isEmpty()){
          List<ReturnOrderDetailVo> detailResponses = order.getReturnOrderDetailVos();
          for (int j = 0,size = detailResponses.size(); j < size; j++) {
            ReturnOrderDetailVo detailResponse = detailResponses.get(j);
            if(j != 0){
              hssfRow = hssfSheet.createRow(total);
            }

            //商品交易号
            String  detailNo = "";
            if (detailResponse.getDetailNo() != null) {
              detailNo = detailResponse.getDetailNo();
            }
            hssfRow.createCell(11).setCellValue(detailNo);

            //商品名称
            String  name = "";
            if (detailResponse.getName() != null) {
              name = detailResponse.getName();
            }
            hssfRow.createCell(12).setCellValue(name);

            //商品编号
            String  skuNo = "";
            if (detailResponse.getSkuNo() != null) {
              skuNo = detailResponse.getSkuNo();
            }
            hssfRow.createCell(13).setCellValue(skuNo);

            //属性规格
            String  specification = "规格：";
            if (detailResponse.getSpecification() != null) {
              specification += detailResponse.getSpecification();
            }
            String color = "颜色：";
            if (detailResponse.getColor() != null) {
              color += detailResponse.getColor();
            }

            String texture = "材质：";
            if (detailResponse.getTexture() != null) {
              texture += detailResponse.getTexture();
            }
            hssfRow.createCell(14).setCellValue(specification+" | "+color+" | "+texture);

            //退货数量
            Integer  refundedQuantity = 0;
            if (detailResponse.getRefundedQuantity() != null) {
              refundedQuantity = detailResponse.getRefundedQuantity();
            }
            hssfRow.createCell(15).setCellValue(refundedQuantity);

            //退款金额
            BigDecimal  amount = new BigDecimal(0);
            if (detailResponse.getRefundedAmount() != null) {
              amount = detailResponse.getRefundedAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            hssfRow.createCell(16).setCellValue(amount.toString());
            total++;
          }
        }
      }
    }
    // 7.将文件输出到客户端浏览器
    workbook.write(out);
    out.flush();
    out.close();
  }
}
