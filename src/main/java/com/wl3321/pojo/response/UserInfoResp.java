package com.wl3321.pojo.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/20 16:11
 * desc   :
 */
@Data
public class UserInfoResp {

    private int id;

    private String name;

    private String headimgurl;

    private String openid;

    private String account_status;

    private String phone;

    private int steps_total;

    private float coin_total;

    private int invite_total;

    private int stepsRank;

    private int inviteRank;

    private boolean isHaveNotice;

    private int userTodayRank;

    private int userTodaySteps;
}
