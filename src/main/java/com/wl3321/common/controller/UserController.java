package com.wl3321.common.controller;

import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/1/27 10:19
 * desc   :
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @PostMapping("/info")
    public ApiResponse userInfo(){
        Test test = new Test();
        test.setName("Super Admin");
        test.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return ApiResponse.ofSuccess(test);
    }
}
