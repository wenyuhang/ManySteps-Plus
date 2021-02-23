package com.wl3321.pojo.request;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 15:59
 * desc   : 用户请求下单
 */
@Data
public class PlaceOrderReq {
    @Min(value = 1,message = "uid 参数不能小于1")
    private int uid;

    @Min(value = 1,message = "pid 参数不能小于1")
    private int pid;

    @Min(value = 1,message = "adid 参数不能小于1")
    private int adid;
}
