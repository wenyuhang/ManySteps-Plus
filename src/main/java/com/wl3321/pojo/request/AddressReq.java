package com.wl3321.pojo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/19 13:53
 * desc   : 收货地址添加/修改
 */
@Data
public class AddressReq {


    @Min(value = 1, message = "uid 参数不能小于1")
    private int uid;

    @NotBlank(message = " receiver 不能为空")
    private String receiver;

    @NotBlank(message = " address 不能为空")
    private String address;

    @NotBlank(message = " mobile 不能为空")
    private String mobile;

    @NotBlank(message = " province 不能为空")
    private String province;

    @NotBlank(message = " city 不能为空")
    private String city;

    @NotBlank(message = " area 不能为空")
    private String area;

    private String post;

    private String createdate;

}
