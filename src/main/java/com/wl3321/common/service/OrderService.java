package com.wl3321.common.service;

import com.wl3321.pojo.entity.Order;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.request.PageReq;
import com.wl3321.pojo.response.OrderResp;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:32
 * desc   :
 */
public interface OrderService {
    String orderKey = "order";
    String orderAllKey = "order:all:";
    String orderPidKey = "order:pid:";
    String orderUidKey = "order:uid:";

    void clearCach(Order order);

    int add(Order order);

    int updateByOrdercode(Order order);

    Order selectByOrdercode(String ordercode);

    List<OrderResp> selectByUidAndDateDesc(PageIDReq req);

    List<OrderResp> selectByPidAndDateDesc(PageIDReq req);

    List<OrderResp> selectByDateDesc(PageReq req);

}
