package com.wl3321.common.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wl3321.common.service.StepsCoinService;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.entity.StepsCoin;
import com.wl3321.pojo.request.PageIDReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/19 12:10
 * desc   :
 */
@RestController
@RequestMapping(value = "/userinfo")
public class StepsCoinController {
    @Autowired
    StepsCoinService stepsCoinService;



    /**
     * 获取用户金币兑换记录
     * @param req
     * @return
     */
    @PostMapping(value = "/coinRecord")
    public ApiResponse list(@Validated @RequestBody PageIDReq req) {
        PageInfo pageInfo = stepsCoinService.selectByUidDateDesc(req);
        return ApiResponse.ofSuccess(pageInfo);
    }
}
