package com.wl3321.common.controller;

import com.github.pagehelper.PageInfo;
import com.wl3321.common.service.InviteRelaService;
import com.wl3321.common.service.RedisService;
import com.wl3321.common.service.StepsRecordService;
import com.wl3321.common.service.UserService;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.Test;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.entity.UserRankInfo;
import com.wl3321.pojo.request.IDReq;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.request.PageReq;
import com.wl3321.pojo.response.InviteRelaResp;
import com.wl3321.pojo.response.UserInfoResp;
import com.wl3321.utils.DateUtils;
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
    @Autowired
    StepsRecordService stepsRecordService;
    @Autowired
    RedisService redisService;

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

    /**
     * 获取用户信息
     * @param req
     * @return
     */
    @PostMapping(value = "/getUserInfo")
    public ApiResponse get(@Validated @RequestBody IDReq req){
        int uid = Integer.parseInt(req.getId());
        User user = userService.selectByUserId(uid);
        if (null==user){
            return ApiResponse.of(999,"用户不存在",null);
        }
        UserInfoResp userInfoResp = new UserInfoResp();

        int code = userService.update(user);
        if (code==0){
            return ApiResponse.of(999,"操作失败，请稍后重试",null);
        }
        //单日步数排名redis-key
        String rankKey = StepsRecordService.stepsRankKey + ":"+DateUtils.getRunDate(System.currentTimeMillis()) ;
        //单日步数排名redis-value
        UserRankInfo rankValue = getRankValue(user);

//        int stepsRanking = userService.getUserStepsRanking(user.getId());
//        int inviteRanking = userService.getUserInviteRanking(user.getId());
        //总步数排名
//        userInfoResp.setStepsRank(stepsRanking);
        //邀请排名
        String inviteRankKey = InviteRelaService.inviteRankKey;
        long inviteRank = redisService.zReverseRank(inviteRankKey, rankValue);
        userInfoResp.setInviteRank((int)inviteRank);
        //查询该用户是否有未读公告
//        boolean isHaveNotices = noticesRecordService.isHaveNotices(user.getId());
//        user.setHaveNotice(isHaveNotices);
        //查询用户今日排名
        long userTodayRank = redisService.zReverseRank(rankKey, rankValue);
        userInfoResp.setUserTodayRank((int)userTodayRank);
        //查询用户今日步数
        double userTodaySteps = redisService.zScore(rankKey, rankValue);
        userInfoResp.setUserTodaySteps((int) userTodaySteps);

        return ApiResponse.ofSuccess(userInfoResp);
    }

    /**
     * 获取zset元素key值
     * @param user
     * @return
     */
    private UserRankInfo getRankValue(User user) {
        UserRankInfo userRankInfo = new UserRankInfo();
        userRankInfo.setId(user.getId());
        userRankInfo.setName(user.getName());
        userRankInfo.setHeadimgurl(user.getHeadimgurl());
        userRankInfo.setOpenid(user.getOpenid());
        return userRankInfo;
    }

}
