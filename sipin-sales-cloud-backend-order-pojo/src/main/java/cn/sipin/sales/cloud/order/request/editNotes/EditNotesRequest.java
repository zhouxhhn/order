/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.editNotes;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EditNotesRequest {

  @ApiModelProperty(value = "备注")
  private String note;
}
