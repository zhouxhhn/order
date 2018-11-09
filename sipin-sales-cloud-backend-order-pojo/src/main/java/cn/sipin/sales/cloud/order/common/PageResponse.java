package cn.sipin.sales.cloud.order.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    private int total;
    private int size;
    private int pages;
    private int current;

    private List<T> records;
}
