package com.wl3321.pojo.entity;

import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 14:26
 * desc   : 订单实体类
 */
@Data
public class Order {

    private int id;

    private int uid;

    private int pid;

    private int adid;

    private int status;

    private String ordercode;

    private String couriernumber;

    private String createdate;
}
