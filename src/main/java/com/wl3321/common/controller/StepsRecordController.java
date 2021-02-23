package com.wl3321.common.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wl3321.common.service.*;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.entity.RunEncryptedData;
import com.wl3321.pojo.entity.StepsRecord;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.entity.UserRankInfo;
import com.wl3321.pojo.request.IDReq;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.request.PageReq;
import com.wl3321.pojo.request.WXRunDataReq;
import com.wl3321.pojo.response.StepsMonitorResp;
import com.wl3321.utils.CoinUtils;
import com.wl3321.utils.DateUtils;
import com.wl3321.utils.WXUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import static com.wl3321.common.service.UserService.userKey;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/19 16:34
 * desc   : 步数换金币
 */
@RestController
@RequestMapping("/userinfo")
public class StepsRecordController {

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    //单日最高转换20000步
    private static int canConvertSteps = 20000;

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    StepsRecordService stepsRecordService;
    @Autowired
    StepsCoinService stepsCoinService;


    /**
     * 计算今日可兑换步数
     *
     * @param req
     * @return
     */
    @Transactional
    @PostMapping(value = "/getRunSteps")
    public ApiResponse getSteps(@Validated @RequestBody WXRunDataReq req) {
        //可兑换的步数
        int steps = 0;
        //今日运动步数
        int stepsToday = 0;
        //今日已兑换步数
        int convertedsteps = 0;

        //校验用户
        User user = userService.selectByUserId(req.getUid());
        if (null == user) {
            return ApiResponse.of(999, "用户不存在", null);
        }
        //获取当前为哪天
        String date = format.format(System.currentTimeMillis());
        StepsRecord stepsRecord = stepsRecordService.selectByUidAndRundate(req.getUid(), date);
        if (null != stepsRecord) {
            stepsToday = stepsRecord.getSteps();
            convertedsteps = stepsRecord.getConvertedsteps();
        } else {
            stepsRecord = new StepsRecord();
            stepsRecord.setUid(req.getUid());
            stepsRecord.setRundate(date);
            stepsRecord.setSteps(stepsToday);
            stepsRecord.setConvertedsteps(convertedsteps);
        }
        //解密运动数据
        String redisKey = userKey+":"+user.getOpenid()+":session_key";
        String session_key = (String) redisService.get(redisKey);
        String json = WXUtils.getUserInfo(req.getData(), session_key, req.getIv());
        RunEncryptedData runData = JSON.parseObject(json, RunEncryptedData.class);
        if (null != runData) {
            List<RunEncryptedData.StepInfoListBean> stepInfoList = runData.getStepInfoList();
            if (!stepInfoList.isEmpty()) {
                RunEncryptedData.StepInfoListBean stepInfoListBean = stepInfoList.get(stepInfoList.size() - 1);
                //比较日期
                long timestamp = stepInfoListBean.getTimestamp();
                if (date.equals(format.format(timestamp * 1000))) {
                    int runSteps = stepInfoListBean.getStep();
                    if (runSteps > stepsToday) {
                        stepsToday = runSteps;
                    }
                }
            }
        }
        //更新用户步数总数，插入数据库
        if (stepsRecord.getSteps()<stepsToday){
            user.setSteps_total(user.getSteps_total()+(stepsToday-stepsRecord.getSteps()));
            user.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
            userService.update(user);
        }

        //检查是否超出限制  可转换步数超出2w步重置为上限2w
        if (canConvertSteps < stepsToday) {
//            stepsToday = canConvertSteps;
            //计算可兑换步数
            steps = canConvertSteps - convertedsteps;
        } else {
            //计算可兑换步数
            steps = stepsToday - convertedsteps;
        }

        //插入数据
        stepsRecord.setSteps(stepsToday);
        int code = 0;
        if (StringUtil.isEmpty(stepsRecord.getCreatedate())) {
            //新增
            stepsRecord.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
            code = stepsRecordService.add(stepsRecord);
        } else {
            //更新
            stepsRecord.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
            code = stepsRecordService.updateByID(stepsRecord);
        }

        if (code == 0) {
            System.out.println("微信步数数据插入失败");
        } else {
            //清除缓存
            stepsRecordService.clearCach(user.getId());
            //加入redis缓存
            String key = StepsRecordService.stepsRecordKey + ":" + user.getId() + ":" + stepsRecord.getRundate();
            redisService.set(key, stepsRecordService.selectByUidAndRundate(user.getId(), stepsRecord.getRundate()));
            //单日步数排行榜
            UserRankInfo userRankInfo = new UserRankInfo();
            userRankInfo.setId(user.getId());
            userRankInfo.setName(user.getName());
            userRankInfo.setHeadimgurl(user.getHeadimgurl());
            userRankInfo.setOpenid(user.getOpenid());
            String rankKey = StepsRecordService.stepsRankKey + ":" + stepsRecord.getRundate();
            redisService.zSet(rankKey, userRankInfo, stepsToday);
            //总步数排行榜
            redisService.zSet(StepsRecordService.stepsTotalRankKey,userRankInfo,user.getSteps_total());
        }

        //注册用户超过1000奖励减半
        if (user.getId() > 1000) {
            return ApiResponse.of(200, "当前运营阶段邀请好友可获得10金币奖励", steps);
        } else {
            return ApiResponse.of(200, "当前运营阶段邀请好友可获得20金币奖励", steps);
        }
    }


    /**
     * 步数转金币
     *
     * @param req
     * @return
     */
    @Transactional
    @PostMapping(value = "/convertSteps")
    public ApiResponse exchange(@Validated @RequestBody IDReq req) {
        //校验用户
        int uid = Integer.parseInt(req.getId());
        User user = userService.selectByUserId(uid);
        if (null == user) {
            return ApiResponse.of(999, "用户不存在", null);
        }
        //获取当前为哪天
        String date = format.format(System.currentTimeMillis());
        StepsRecord stepsRecord = stepsRecordService.selectByUidAndRundate(uid, date);
        if (null == stepsRecord) {
            return ApiResponse.of(999, "兑换金币出现问题，请退出重试", null);
        }
        //步数转换金币
        int steps = 0;
        int stepsToday = stepsRecord.getSteps();
        //检查是否超出限制  可转换步数超出2w步重置为上限2w
        if (canConvertSteps < stepsToday) {
            steps = canConvertSteps - stepsRecord.getConvertedsteps();
        }else {
            steps = stepsToday - stepsRecord.getConvertedsteps();
        }

        float coin = CoinUtils.calc(steps);
        int convertedsteps = stepsRecord.getConvertedsteps() + steps;
        stepsRecord.setConvertedsteps(convertedsteps);
        stepsRecord.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        //更新步数记录
        int code = stepsRecordService.updateByID(stepsRecord);
        if (code == 0) {
            System.out.println("微信步数数据更新失败");
            return ApiResponse.of(999, "兑换金币出现问题，请退出重试", null);
        }
        //发放奖励
        int scode = stepsCoinService.add(user, "步数转金币", coin);
        if (scode != 0){
            //清除缓存
            stepsRecordService.clearCach(uid);
        }
        return ApiResponse.ofSuccess(0);
    }

    /**
     * 获取监控警告
     * @param req
     * @return
     */
    @PostMapping(value = "/getMonitorsData")
    public ApiResponse getMonitorsData(@Validated @RequestBody PageReq req){
        List<StepsMonitorResp> list = stepsRecordService.selectStepsMonitor(req);
        PageInfo pageInfo = new PageInfo(list);
        return ApiResponse.ofSuccess(pageInfo);
    }


    /**
     * 获取用户步数记录
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/stepsRecord", method = RequestMethod.POST)
    public ApiResponse list(@Validated @RequestBody PageIDReq req) {
        List<StepsRecord> list = stepsRecordService.selectByUidDesc(req);
        PageInfo pageInfo = new PageInfo(list);
        return ApiResponse.ofSuccess(pageInfo);
    }

}
