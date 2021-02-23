package com.wl3321.common.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wl3321.common.service.*;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.entity.Order;
import com.wl3321.pojo.entity.Product;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.*;
import com.wl3321.pojo.response.OrderResp;
import com.wl3321.utils.DateUtils;
import com.wl3321.utils.RandomNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 14:25
 * desc   : 订单模块
 */
@Validated
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    StepsCoinService stepsCoinService;
    @Autowired
    NoticesRecordService noticesRecordService;

    /**
     * 下单前 查询余额
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/checkBalance")
    public ApiResponse check(@Validated @RequestBody CheckBalanceReq req) {
        User user = userService.selectByUserId(req.getUid());
        if (null == user) {
            return ApiResponse.of(999, "用户不存在", null);
        }
        Product product = productService.selectById(req.getPid());
        if (null == product) {
            return ApiResponse.of(999, "商品不存在", null);
        }
        float coin = product.getCoin();
        float coin_total = user.getCoin_total();

        if (coin_total < coin) {
            return ApiResponse.of(999, "金币余额不足", null);
        }
        return ApiResponse.ofSuccess(null);
    }

    /**
     * 用户进行下单 扣款
     *
     * @param req
     * @return
     */
    @Transactional
    @PostMapping(value = "/placeOrder")
    public ApiResponse placeOrder(@Validated @RequestBody PlaceOrderReq req) {
        User user = userService.selectByUserId(req.getUid());
        if (null == user) {
            return ApiResponse.of(999, "用户不存在", null);
        }
        Product product = productService.selectById(req.getPid());
        if (null == product) {
            return ApiResponse.of(999, "商品不存在", null);
        }
        float coin = product.getCoin();
        int stock = product.getStock();
        float coin_total = user.getCoin_total();
        //清点库存
        if (stock <= 0) {
            return ApiResponse.of(999, "商品已被兑换完毕，工作人员正在加紧补货！~", null);
        }
        //检查余额
        if (coin_total < coin) {
            return ApiResponse.of(999, "金币余额不足", null);
        }
        //创建订单
        Order order = new Order();
        order.setUid(req.getUid());
        order.setPid(req.getPid());
        order.setAdid(req.getAdid());
        order.setOrdercode(RandomNumber.getInstance().GetRandom());
        order.setStatus(10);
        order.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        int code = orderService.add(order);
        if (code == 0) {
            return ApiResponse.of(999, "操作失败请重试", null);
        }
        //订单插入成功 进行扣款
        stepsCoinService.add(user, "购买" + product.getName() + "划扣", -coin);
        //商品进行库存划扣
        product.setStock(stock - 1);
        productService.update(product);
        //下单完成 清除指定缓存
        orderService.clearCach(order);

        return ApiResponse.ofSuccess(order);
    }

    /**
     * 后台操作退单处理
     * @param req
     * @return
     */
    @Transactional
    @PostMapping(value = "/chargeBack")
    public ApiResponse chargeBack(@Validated @RequestBody ChargeBackReq req){
        Order order = orderService.selectByOrdercode(req.getOrdercode());
        if (null == order) {
            return ApiResponse.of(999, "订单不存在", null);
        }
        User user = userService.selectByUserId(order.getUid());
        if (null == user) {
            return ApiResponse.of(999, "用户不存在", null);
        }
        Product product = productService.selectById(order.getPid());
        if (null == user) {
            return ApiResponse.of(999, "商品不存在", null);
        }
        //交易系统退还金额
        stepsCoinService.add(user,product.getName()+"订单退还处理",product.getCoin());
        //插入公告
        noticesRecordService.add(user,req.getDesc(),0,product.getCoin());
        //更新订单信息
        order.setCouriernumber("订单退还处理");
        order.setStatus(30);
        order.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        orderService.updateByOrdercode(order);
        //清理缓存
        orderService.clearCach(order);
        return ApiResponse.ofSuccess(null);
    }

    /**
     * 修改订单信息
     * @param ordercode              订单号
     * @param couriernumber          快递单号
     * @param status                 订单状态
     * @return
     */
    @Transactional
    @RequestMapping(value = "/updateOrderNum", method = RequestMethod.POST)
    public ApiResponse update(@NotBlank(message = "ordercode 不能为空")  String ordercode,
                              @NotBlank(message = "couriernumber 不能为空") String couriernumber,
                              @Min(value = 10, message = "status 不能小于10") int status) {

        //根据单号查询订单
        Order order = orderService.selectByOrdercode(ordercode);
        if (null == order) {
            return ApiResponse.of(999, "该订单不存在", null);
        }
        //更新订单信息
        order.setCouriernumber(couriernumber);
        order.setStatus(status);
        order.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        int code = orderService.updateByOrdercode(order);
        if (code==0){
            return ApiResponse.of(999,"操作失败请重试",null);
        }
        //清理缓存
        orderService.clearCach(order);
        return ApiResponse.ofSuccess(order);
    }

    /**
     * 根据用户id获取订单
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/myOrder")
    public ApiResponse listByUser(@Validated @RequestBody PageIDReq req) {
        List<OrderResp> list = orderService.selectByUidAndDateDesc(req);
        PageInfo pageInfo = new PageInfo(list);
        return ApiResponse.ofSuccess(pageInfo);
    }

    /**
     * 根据商品id获取订单
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/orderListByProduct")
    public ApiResponse selectByPidAndDateDesc(@Validated @RequestBody PageIDReq req) {
        List<OrderResp> list = orderService.selectByPidAndDateDesc(req);
        PageInfo pageInfo = new PageInfo(list);
        return ApiResponse.ofSuccess(pageInfo);
    }

    /**
     * 获取全部订单
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/orderList")
    public ApiResponse list(@Validated @RequestBody PageReq req) {
        List<OrderResp> list = orderService.selectByDateDesc(req);
        PageInfo pageInfo = new PageInfo(list);
        return ApiResponse.ofSuccess(pageInfo);
    }
}
