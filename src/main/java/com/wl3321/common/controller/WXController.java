package com.wl3321.common.controller;

import com.alibaba.fastjson.JSON;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.response.WXCodeSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/1 17:54
 * desc   :
 */
@RestController
public class WXController {
    //小程序配置
    private String WX_APP_APPID = "wx7daac9b7e03f1cf4";
    private String WX_APP_SECRET = "1805a3ea9fddfa40288a5293f5dc9071";

    @Autowired
    RestTemplate restTemplate;


    /**
     * 微信登录 注册
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/wxlogin")
    public ApiResponse wxLogin(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WX_APP_APPID + "&secret=" + WX_APP_SECRET + "&js_code=" + code + "&grant_type=authorization_code";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        WXCodeSessionBean wxCodeSessionBean = JSON.parseObject(response.getBody(), WXCodeSessionBean.class);

        return ApiResponse.of(wxCodeSessionBean.getErrcode(),wxCodeSessionBean.getErrmsg(),null);
//        return ApiResponse.ofSuccess(response.getBody());
    }
}
