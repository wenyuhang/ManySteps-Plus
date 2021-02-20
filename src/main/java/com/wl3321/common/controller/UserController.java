package com.wl3321.common.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wl3321.common.service.InviteRelaService;
import com.wl3321.common.service.UserService;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.Test;
import com.wl3321.pojo.entity.InviteRela;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.request.PageReq;
import com.wl3321.pojo.response.InviteRelaResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/1/27 10:19
 * desc   :
 */
@RestController
@RequestMapping(value = "/userinfo")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    InviteRelaService inviteRelaService;

    @PostMapping("/info")
    public ApiResponse userInfo(){
        Test test = new Test();
        test.setName("Super Admin");
        test.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return ApiResponse.ofSuccess(test);
    }


    /**
     * 获取用户列表
     * @param req
     * @return
     */
    @RequestMapping(value = "/userList",method = RequestMethod.POST)
    public ApiResponse selectByCreateDesc(@Validated @RequestBody PageReq req){
        List<User> list = userService.selectByDateDesc(req);
        PageInfo pageInfo = new PageInfo(list);
        return ApiResponse.ofSuccess(pageInfo);
    }

    /**
     * 获取用户的邀请记录
     * @param req
     * @return
     */
    @PostMapping(value = "/getInviteRecord")
    public ApiResponse getInviteRecord(@Validated @RequestBody PageIDReq req){
        List<InviteRelaResp> list = inviteRelaService.selectByInviteID(req);
        PageInfo pageInfo=new PageInfo(list);
        return ApiResponse.ofSuccess(pageInfo);
    }

//    /**
//     * 获取用户信息
//     * @param req
//     * @return
//     */
//    @PostMapping(value = "/getUserInfo")
//    public ApiResponse get(@Validated @RequestBody IDReq req){
//        User user = userService.get(req.getId());
//        if (null==user){
//            return ApiResponse.of(999,"用户不存在",null);
//        }
//        //计算金币总和
//        float coin_total = stepsCoinService.sum(user.getId());
//        //计算步数总和
//        int steps_total = stepsRecordService.sum(user.getId());
//        //计算邀请人数个和
//        int invite_total = inviteRelaService.getInviteNum(user.getId());
//        //更新用户数据库
//        user.setCoin_total(coin_total);
//        user.setSteps_total(steps_total);
//        user.setInvite_total(invite_total);
//        int code = userService.update(user);
//        if (code==0){
//            return ApiResponse.of(999,"操作失败，请稍后重试",null);
//        }
//        int stepsRanking = userService.getUserStepsRanking(user.getId());
//        int inviteRanking = userService.getUserInviteRanking(user.getId());
//        user.setStepsRank(stepsRanking);
//        user.setInviteRank(inviteRanking);
//        //查询该用户是否有未读公告
//        boolean isHaveNotices = noticesRecordService.isHaveNotices(user.getId());
//        user.setHaveNotice(isHaveNotices);
//        //查询用户今日排名
//        int userTodayRank = stepsRecordService.getUserTodayRank(user.getId());
//        user.setUserTodayRank(userTodayRank);
//        //查询用户今日步数
//        int userTodaySteps = stepsRecordService.getUserTodaySteps(user.getId());
//        user.setUserTodaySteps(userTodaySteps);
//
//        return ApiResponse.ofSuccess(user);
//    }

}
