package com.wl3321.pojo.request;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 15:56
 * desc   : 查询余额请求
 */
@Data
public class CheckBalanceReq {

    @Min(value = 1,message = "uid 参数不能小于1")
    private int uid;

    @Min(value = 1,message = "pid 参数不能小于1")
    private int pid;
}
