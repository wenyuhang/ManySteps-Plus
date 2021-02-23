package com.wl3321.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wl3321.common.mapper.OrderMapper;
import com.wl3321.common.service.OrderService;
import com.wl3321.common.service.ProductService;
import com.wl3321.common.service.RedisService;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.entity.Order;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.request.PageReq;
import com.wl3321.pojo.response.OrderResp;
import com.wl3321.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:33
 * desc   :
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisService redisService;
    @Autowired
    OrderMapper orderMapper;


    /**
     * 清理缓存
     */
    @Override
    public void clearCach(Order order) {
        //清除单个订单缓存
        redisService.del(OrderService.orderKey+":"+order.getOrdercode());
        //清理全部订单缓存
        Set<String> allKeys = redisService.keys(OrderService.orderAllKey + "*");
        redisService.del(allKeys);
        //清理指定用户订单缓存
        Set<String> uidKeys = redisService.keys(OrderService.orderUidKey+order.getUid()+ "*");
        redisService.del(uidKeys);
        //清理指定商品订单缓存
        Set<String> pidKeys = redisService.keys(OrderService.orderPidKey+order.getPid()+ "*");
        redisService.del(pidKeys);
    }

    /**
     * 创建订单
     * @param order
     * @return
     */
    @Override
    public int add(Order order) {
        return orderMapper.add(order);
    }

    /**
     * 更新订单信息
     * @param order
     * @return
     */
    @Override
    public int updateByOrdercode(Order order) {
        return orderMapper.updateByOrdercode(order);
    }

    /**
     * 根据订单号查询订单详情
     * @param ordercode
     * @return
     */
    @Override
    public Order selectByOrdercode(String ordercode) {
        return orderMapper.selectByOrdercode(ordercode);
    }

    /**
     * 根据用户id倒序查询订单
     * @param req
     * @return
     */
    @Override
    public List<OrderResp> selectByUidAndDateDesc(PageIDReq req) {
        //redis-key
        int uid = Integer.parseInt(req.getId());
        String key = OrderService.orderUidKey + uid + ":" + req.getPage() + ":" + req.getSize();
        List<OrderResp> list = (List<OrderResp>) redisService.get(key);
        if (list == null) {
            PageHelper.startPage(req.getPage(), req.getSize());
            list = orderMapper.selectByUidAndDateDesc(uid);
            PageHelper.clearPage();
            redisService.set(key, list);
        }
        return list;
    }

    /**
     * 根据商品id倒序查询订单
     *
     * @param req
     * @return
     */
    @Override
    public List<OrderResp> selectByPidAndDateDesc(PageIDReq req) {
        //redis-key
        int pid = Integer.parseInt(req.getId());
        String key = OrderService.orderPidKey + pid + ":" + req.getPage() + ":" + req.getSize();
        List<OrderResp> list = (List<OrderResp>) redisService.get(key);
        if (list == null) {
            PageHelper.startPage(req.getPage(), req.getSize());
            list = orderMapper.selectByPidAndDateDesc(pid);
            PageHelper.clearPage();
            //APP端请求 转换距今日期
            for (OrderResp order : list) {
                String str = DateUtils.calcDistanceTime(order.getCreatedate());
                order.setDistancedate(str);
            }
            redisService.set(key, list);
        }
        return list;
    }

    /**
     * 根据日期倒序查询全部订单
     *
     * @param req
     * @return
     */
    @Override
    public List<OrderResp> selectByDateDesc(PageReq req) {
        //redis-key
        String key = OrderService.orderAllKey + req.getPage() + ":" + req.getSize();
        List<OrderResp> list = (List<OrderResp>) redisService.get(key);
        if (list == null) {
            PageHelper.startPage(req.getPage(), req.getSize());
            list = orderMapper.selectByDateDesc();
            PageHelper.clearPage();
            redisService.set(key, list);
        }
        return list;
    }

}
