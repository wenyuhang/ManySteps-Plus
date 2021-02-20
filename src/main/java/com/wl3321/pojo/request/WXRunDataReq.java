package com.wl3321.pojo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/19 16:35
 * desc   : 微信步数解密
 */
@Data
public class WXRunDataReq {
    @Min(value = 1, message = "uid 参数不能为空")
    private int uid;

    @NotBlank(message = "data 参数不能为空")
    private String data;

    @NotBlank(message = "iv 参数不能为空")
    private String iv;
}
