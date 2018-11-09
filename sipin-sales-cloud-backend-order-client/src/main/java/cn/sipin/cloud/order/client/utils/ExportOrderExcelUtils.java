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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import cn.sipin.sales.cloud.order.constants.OrderDiscountTypeId;
import cn.sipin.sales.cloud.order.pojo.OrderDiscount;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.response.SalesOrderDetailResponse;
import cn.sipin.sales.cloud.order.response.SalesOrderDiscountResponse;
import cn.sipin.sales.cloud.order.response.backend.index.IndexOrdersResponse;

public class ExportOrderExcelUtils {


  public static void export(HttpServletResponse res, List<IndexOrdersResponse> ordersList) throws Exception{
    String[] orderTitles = { "销售订单号", "门店", "经销商", "会员", "下单时间", "订单状态", "导购员", "操作员","合计金额", "会员折扣优惠",
                             "优惠券优惠金额", "积分减免金额", "整单优惠金额", "应付金额", "已付金额", "备注"};
    String[] orderDetailTitles = {"商品交易号", "商品名称","商品编号", "属性规格", "交付标签", "是否赠品",
                                  "商品数量", "售价", "现价","小计", "实际交易金额"};

    ServletOutputStream out=res.getOutputStream();
    String fileName=new String((new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getBytes(), "UTF-8");
    res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    res.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");

    // 1.创建一个workbook，对应一个Excel文件
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 2.在webbook中添加一个sheet,对应Excel文件中的sheet
    HSSFSheet hssfSheet = workbook.createSheet("销售订单");
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
        IndexOrdersResponse order = ordersList.get(i);
        // 6.创建单元格，并设置值

        //销售订单号
        String no = "";
        if (order.getNo() != null) {
          no = order.getNo() + "";
        }
        hssfRow.createCell(0).setCellValue(no);

        //门店
        String shopName = "";
        if (order.getShopName() != null) {
          shopName = order.getShopName();
        }
        hssfRow.createCell(1).setCellValue(shopName);

        //经销商
        String agencyName = "";
        if (order.getAgencyName() != null) {
          agencyName = order.getAgencyName();
        }
        hssfRow.createCell(2).setCellValue(agencyName);

        //会员
        String mobile = "";
        if (order.getMobile() != null) {
          mobile = order.getMobile();
        }
        hssfRow.createCell(3).setCellValue(mobile);

        //下单时间
        String paidAt = "";
        if (order.getPaidAt() != null) {
          paidAt = order.getPaidAt().format(formatter);
        }
        hssfRow.createCell(4).setCellValue(paidAt);

        //订单状态
        String status = "";
        if (order.getStatus() != null) {
          status = order.getStatus();
        }
        hssfRow.createCell(5).setCellValue(status);

        //导购员
        String salerName = "";
        if (order.getStatus() != null) {
          salerName = order.getSalerName();
        }
        hssfRow.createCell(6).setCellValue(salerName);

        //操作员
        String createrName = "";
        if (order.getCreaterName() != null) {
          createrName = order.getCreaterName();
        }
        hssfRow.createCell(7).setCellValue(createrName);

        //合计金额
        BigDecimal amount = new BigDecimal(0);
        if (order.getAmount() != null) {
          amount = order.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(8).setCellValue(amount.toString());

        //会员折扣优惠
        BigDecimal discount = new BigDecimal(0);
        hssfRow.createCell(9).setCellValue(discount.toString());

        //优惠券优惠金额
        BigDecimal coupon = new BigDecimal(0);

        //积分减免金额
        BigDecimal point = new BigDecimal(0);
        if(order.getOrderDiscountList() != null && !order.getOrderDiscountList().isEmpty()){
          for (int j = 0,size = order.getOrderDiscountList().size(); j < size; j++) {
            SalesOrderDiscountResponse response = order.getOrderDiscountList().get(j);
            //优惠券
            if(OrderDiscountTypeId.COUPON_MONEY.getValue() == response.getTypeId()){
              coupon = coupon.add(response.getDiscountValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }else if(OrderDiscountTypeId.COUPON_DISCOUNT.getValue() == response.getTypeId()){
              coupon = coupon.add(order.getAmount().multiply(new BigDecimal(1).subtract(response.getDiscountValue())).setScale(2, BigDecimal.ROUND_HALF_UP));
            }else if(OrderDiscountTypeId.PONIT_MONEY.getValue() == response.getTypeId()){
              //积分
              point = point.add(response.getDiscountValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
          }
        }
        hssfRow.createCell(10).setCellValue(coupon.toString());

        //积分减免金额
        hssfRow.createCell(11).setCellValue(point.toString());

        //整单优惠金额
        BigDecimal whole = new BigDecimal(0);
        if (order.getOriginalPayableAmount() != null && order.getPayableAmount() != null) {
          whole = order.getOriginalPayableAmount().subtract(order.getPayableAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(12).setCellValue(whole.toString());

        //应付金额
        BigDecimal payableAmount = new BigDecimal(0);
        if (order.getPayableAmount() != null) {
          payableAmount = order.getPayableAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(13).setCellValue(payableAmount.toString());

        //已付金额
        BigDecimal paidAmount = new BigDecimal(0);
        if (order.getPaidAmount() != null) {
          paidAmount = order.getPaidAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        hssfRow.createCell(14).setCellValue(paidAmount.toString());

        //备注
        String  note = "";
        if (order.getNote() != null) {
          note = order.getNote();
        }
        if (order.getAdminNote() != null) {
          note +=" | 管理员:"+order.getAdminNote();
        }

        hssfRow.createCell(15).setCellValue(note);

        //订单金额
        if(order.getOrderDetailList() != null && !order.getOrderDetailList().isEmpty()){
          List<SalesOrderDetailResponse> detailResponses = order.getOrderDetailList();
          for (int j = 0,size = detailResponses.size(); j < size; j++) {
            SalesOrderDetailResponse detailResponse = detailResponses.get(j);
            if(j != 0){
              hssfRow = hssfSheet.createRow(total);
            }

            //商品交易号
            String  detailNo = "";
            if (detailResponse.getDetailNo() != null) {
              detailNo = detailResponse.getDetailNo();
            }
            hssfRow.createCell(16).setCellValue(detailNo);

            //商品名称
            String  name = "";
            if (detailResponse.getName() != null) {
              name = detailResponse.getName();
            }
            hssfRow.createCell(17).setCellValue(name);

            //商品编号
            String  skuNo = "";
            if (detailResponse.getSkuNo() != null) {
              skuNo = detailResponse.getSkuNo();
            }
            hssfRow.createCell(18).setCellValue(skuNo);

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
            hssfRow.createCell(19).setCellValue(specification+" | "+color+" | "+texture);

            //交付标签 TODO
            String  flag = "";
            if (detailResponse.getSkuSn() != null) {
              flag = detailResponse.getSkuSn();
            }
            hssfRow.createCell(20).setCellValue(flag);

            //是否赠品
            String  isGift = "否";
            if (detailResponse.getIsGift() != null && detailResponse.getIsGift()) {
              isGift = "是";
            }
            hssfRow.createCell(21).setCellValue(isGift);

            //商品数量
            String  quantity = "";
            if (detailResponse.getQuantity() != null) {
              quantity = detailResponse.getQuantity()+"";
            }
            hssfRow.createCell(22).setCellValue(quantity);

            //售价
            BigDecimal  originalAmount = new BigDecimal(0);
            if (detailResponse.getAmount() != null) {
              originalAmount = detailResponse.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            hssfRow.createCell(23).setCellValue(originalAmount.toString());

            //现价
            BigDecimal  discountAmount = new BigDecimal(0);
            if (detailResponse.getDiscountAmount() != null) {
              discountAmount = detailResponse.getDiscountAmount();
            }
            hssfRow.createCell(24).setCellValue(discountAmount.toString());

            //小计
            BigDecimal  subtotal =  new BigDecimal(0);
            if (detailResponse.getSkuNo() != null) {
              subtotal = detailResponse.getSubtotal().setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            hssfRow.createCell(25).setCellValue(subtotal.toString());

            //实际交易金额
            BigDecimal  realAmount =  new BigDecimal(0);
            if (detailResponse.getRealAmount() != null) {
              realAmount = detailResponse.getRealAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            hssfRow.createCell(26).setCellValue(realAmount.toString());
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
