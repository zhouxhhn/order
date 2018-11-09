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
import cn.sipin.sales.cloud.order.constants.RefundType;
import cn.sipin.sales.cloud.order.response.backend.index.IndexSalesPaymentResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.sipin.sales.cloud.order.response.vo.ReturnOrderDetailVo;

public class ExportOrderPaymentExcelUtils {


  public static void export(HttpServletResponse res, List<IndexSalesPaymentResponse> paymentResponseList) throws Exception{
    String[] orderTitles = {"销售订单号", "交易流水号", "交易时间", "经销商","门店",  "交易类型", "交易方式", "交易金额", "找零","实际发生金额", "操作员"};

    ServletOutputStream out=res.getOutputStream();
    String fileName=new String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), "UTF-8");
    res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    res.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");

    // 1.创建一个workbook，对应一个Excel文件
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 2.在webbook中添加一个sheet,对应Excel文件中的sheet
    HSSFSheet hssfSheet = workbook.createSheet("交易流水");
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


    // 5.写入实体数据
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    int total = 1;

    if (paymentResponseList != null && paymentResponseList.size() > 0) {
      for (int i = 0; i < paymentResponseList.size(); i++) {
        hssfRow = hssfSheet.createRow(total);
        IndexSalesPaymentResponse payment = paymentResponseList.get(i);

        //销售订单号
        String no = "";
        if (payment.getNo() != null) {
          no = payment.getNo() + "";
        }
        hssfRow.createCell(0).setCellValue(no);

        //交易流水号
        String paymentNo = "";
        if (payment.getPaymentNo()!= null) {
          paymentNo = payment.getPaymentNo();
        }
        hssfRow.createCell(1).setCellValue(paymentNo);

        //交易时间
        String paidAt = "";
        if (payment.getPaidAt()!= null) {
          paidAt = payment.getPaidAt().format(formatter);
        }
        hssfRow.createCell(2).setCellValue(paidAt);


        //经销商
        String agencyName = "";
        if (payment.getAgencyName() != null) {
          agencyName = payment.getAgencyName();
        }
        hssfRow.createCell(3).setCellValue(agencyName);

        //门店
        String shopName = "";
        if (payment.getShopName() != null) {
          shopName = payment.getShopName();
        }
        hssfRow.createCell(4).setCellValue(shopName);

        //交易类型
        String exchangeType = "";
        if (payment.getExchangeType() != null) {
          exchangeType = payment.getExchangeType();
        }
        hssfRow.createCell(5).setCellValue(exchangeType);

        //交易方式
        String exchangeMode = "";
        if (payment.getExchangeMode() != null) {
          exchangeMode = payment.getExchangeMode();
        }
        hssfRow.createCell(6).setCellValue(exchangeMode);

        //交易金额
        BigDecimal price = new BigDecimal(0);
        if (payment.getPrice() != null) {
          price = payment.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(7).setCellValue(price.toString());

        //找零
        BigDecimal giveChange = new BigDecimal(0);
        if (payment.getGiveChange() != null) {
          giveChange = payment.getGiveChange().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(8).setCellValue(giveChange.toString());

        //实际发生金额
        BigDecimal realReceivePrice = new BigDecimal(0);
        if (payment.getRealReceivePrice() != null) {
          realReceivePrice = payment.getRealReceivePrice().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(9).setCellValue(realReceivePrice.toString());

        //操作员
        String operator = "";
        if (payment.getOperator() != null) {
          operator = payment.getOperator();
        }
        hssfRow.createCell(10).setCellValue(operator);
        total++;
      }
    }
    // 7.将文件输出到客户端浏览器
    workbook.write(out);
    out.flush();
    out.close();
  }
}
