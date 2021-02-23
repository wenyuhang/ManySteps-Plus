package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.Order;
import com.wl3321.pojo.response.OrderResp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:32
 * desc   : 订单系统
 */
@Mapper
@Repository
public interface OrderMapper {

    int add(Order order);

    int updateByOrdercode(Order order);

    Order selectByOrdercode(String ordercode);

    List<OrderResp> selectByUidAndDateDesc(int uid);

    List<OrderResp> selectByPidAndDateDesc(int pid);

    List<OrderResp> selectByDateDesc();

}
