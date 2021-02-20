package com.wl3321.pojo.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 12:01
 * desc   : 微信登录/注册
 */
@Data
public class LoginReq {

    @NotBlank(message = "code 参数不能为空")
    private String code;

    private String encryptedData;//加密数据

    private String iv;//解密向量

    private int referrer;//推荐人id
}
