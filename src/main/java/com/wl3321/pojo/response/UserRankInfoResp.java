package com.wl3321.pojo.response;

import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 17:20
 * desc   :
 */
@Data
public class UserRankInfoResp {
    private int id;

    private String name;

    private String headimgurl;

    private String openid;

    private int steps_total;

    private int invite_total;
}
