package com.wl3321.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.wl3321.common.service.*;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.entity.InviteRela;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.entity.UserRankInfo;
import com.wl3321.pojo.request.LoginReq;
import com.wl3321.pojo.response.WXCodeSessionBean;
import com.wl3321.utils.DateUtils;
import com.wl3321.utils.WXUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static com.wl3321.common.service.UserService.userKey;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/1 17:54
 * desc   :
 */
@Validated
@RestController
public class WXController {
    //小程序配置
    private String WX_APP_APPID = "wx7daac9b7e03f1cf4";
    private String WX_APP_SECRET = "1805a3ea9fddfa40288a5293f5dc9071";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;
    @Autowired
    InviteRelaService inviteRelaService;
    @Autowired
    StepsCoinService stepsCoinService;


    /**
     * 微信登录 注册
     *
     * @return
     */
    @Transactional
    @PostMapping(value = "/wxlogin")
    public ApiResponse wxLogin(@Validated @RequestBody LoginReq req) {
        //换取 用户唯一标识 OpenID 和 会话密钥 session_key。
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WX_APP_APPID + "&secret=" + WX_APP_SECRET + "&js_code=" + req.getCode() + "&grant_type=authorization_code";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        //获取session_key
        WXCodeSessionBean wxCodeSessionBean = JSON.parseObject(response.getBody(), WXCodeSessionBean.class);
        if (wxCodeSessionBean.getErrcode() != 0) {
            return ApiResponse.of(wxCodeSessionBean.getErrcode(), wxCodeSessionBean.getErrmsg(), null);
        }
        //redis 存储session_key
        String session_key = wxCodeSessionBean.getSession_key();
        String redisKey = userKey+":"+wxCodeSessionBean.getOpenid()+":session_key";
        redisService.set(redisKey,session_key);
        //查询此用户是否为新用户
        User user = userService.selectByOpenid(wxCodeSessionBean.getOpenid());
        if (null == user) {
            //新用户
            //解密用户信息
            JSONObject userInfo = JSONObject.parseObject(WXUtils.getUserInfo(req.getEncryptedData(), session_key, req.getIv()));
            System.out.println(userInfo.toJSONString());
            if (userInfo == null) {
                return ApiResponse.of(999, "数据解析错误，请稍后重试", null);
            }
            //创建新用户数据
            user = new User();
            //注意处理微信表情
            String nickName = (String) userInfo.get("nickName");
            user.setName(StringUtil.isEmpty(nickName) ? "" : nickName);
            String avatarUrl = (String) userInfo.get("avatarUrl");
            user.setHeadimgurl(avatarUrl);
//            String openId = (String) userInfo.get("openId");
            user.setOpenid(wxCodeSessionBean.getOpenid());
//            String unionId = (String) userInfo.get("unionId");
            user.setUnionid(StringUtil.isEmpty(wxCodeSessionBean.getUnionid()) ? "" : wxCodeSessionBean.getUnionid());
            user.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
            user.setSession_key(session_key);
            //插入新用户数据
            int code = userService.add(user);
            if (code == 0) {
                return ApiResponse.of(999, "操作失败请重试", null);
            }
            //查询用户完整信息 userId
            user = userService.selectByOpenid(user.getOpenid());
            //查询邀请人信息
            User inviteUser = userService.selectByUserId(req.getReferrer());
            if (null != inviteUser){
                //绑定邀请关系
                InviteRela inviteRela = new InviteRela();
                inviteRela.setUid(user.getId());
                inviteRela.setInviter_id(req.getReferrer());
                inviteRela.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
                //插入数据
                int icode = inviteRelaService.add(inviteRela);
                if (icode != 0) {
                    //发放邀请奖励
                    //注册用户超过1000奖励减半
                    if (user.getId()>1000){
                        //发放奖励
                        stepsCoinService.add(inviteUser, "邀请" + (StringUtil.isEmpty(nickName) ? "用户"+user.getId() : nickName) + "奖励", 10);
                    }else {
                        //发放奖励
                        stepsCoinService.add(inviteUser, "邀请" + (StringUtil.isEmpty(nickName) ? "用户"+user.getId() : nickName) + "奖励", 20);
                    }
                }
                //更新用户邀请总数
                inviteUser.setInvite_total(inviteUser.getInvite_total()+1);
                inviteUser.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
                userService.update(inviteUser);
                //更新用户排行榜数据
                String inviteRankKey = InviteRelaService.inviteRankKey;
                redisService.zIncrby(inviteRankKey,getRankValue(inviteUser),1);
            }
        } else {
            //老用户
            //更新sessionkey 更新用户信息
            user.setSession_key(session_key);
            int code = userService.update(user);
            if (code == 0) {
                return ApiResponse.of(999, "操作失败请重试", null);
            }
        }

        return ApiResponse.ofSuccess(user);
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
