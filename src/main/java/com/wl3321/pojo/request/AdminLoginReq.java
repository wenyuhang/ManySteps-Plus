package com.wl3321.pojo.request;

import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/1/19 10:22
 * desc   : 后台管理员登录请求
 */
@Data
public class AdminLoginReq {

    private Object type;

    private String username;

    private String password;
}
