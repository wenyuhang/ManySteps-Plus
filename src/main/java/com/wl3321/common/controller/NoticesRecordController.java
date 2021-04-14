package com.wl3321.common.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wl3321.common.service.NoticesRecordService;
import com.wl3321.common.service.StepsCoinService;
import com.wl3321.common.service.UserService;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.entity.NoticesRecord;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.IDReq;
import com.wl3321.pojo.request.PageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/23 10:09
 * desc   :
 */
@RestController
@RequestMapping(value = "/notices")
public class NoticesRecordController {

    @Autowired
    NoticesRecordService noticesRecordService;
    @Autowired
    UserService userService;
    @Autowired
    StepsCoinService stepsCoinService;

//    public ApiResponse add(){
//        //检查用户
//        User user = userService.selectByUserId(req.getUid());
//        if (null == user) {
//            return ApiResponse.of(999, "用户不存在", null);
//        }
//
//        return ApiResponse.ofSuccess(null);
//    }

    /**
     * 用户获取未读公告
     *
     * @param req
     * @return
     */
    @Transactional
    @PostMapping(value = "/getUserNoticesRecord")
    public ApiResponse getUserRecord(@Validated @RequestBody IDReq req) {
        //用户获取未读公告
        int uid = Integer.parseInt(req.getId());
        NoticesRecord noticesRecord = noticesRecordService.selectUserNotice(uid);
        if (noticesRecord != null){
            //清除未读标记
            noticesRecord.setP_status(true);
            noticesRecord.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            noticesRecordService.updateById(noticesRecord);
            noticesRecordService.clearUserCach(uid);
        }
        return ApiResponse.ofSuccess(noticesRecord);
    }

    /**
     * 分页查询全部公告
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/getNoticesRecord")
    public ApiResponse list(@Validated @RequestBody PageReq req) {
        PageInfo pageInfo = noticesRecordService.selectByDateDesc(req);
        return ApiResponse.ofSuccess(pageInfo);
    }
}
