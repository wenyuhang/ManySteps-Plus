package com.wl3321.pojo.response;

import com.wl3321.pojo.entity.Product;
import com.wl3321.pojo.entity.User;
import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 15:38
 * desc   : 订单返回数据类
 */
@Data
public class OrderResp {

    private int id;
    private int uid;
    private int pid;
    private int adid;
    private int status;
    private String ordercode;
    private String couriernumber;
    private String createdate;

    private Product product;

    private User user;

    private String distancedate;
}
