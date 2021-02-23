package com.wl3321.pojo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/23 11:45
 * desc   : 退单处理
 */
@Data
public class ChargeBackReq {

    @NotBlank(message = "ordercode 参数不能为空")
    private String ordercode;

    @NotBlank(message = "desc 参数不能为空")
    private String desc;
}
