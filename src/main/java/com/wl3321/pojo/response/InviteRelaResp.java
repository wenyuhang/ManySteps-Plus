package com.wl3321.pojo.response;

import com.wl3321.pojo.entity.User;
import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 15:38
 * desc   : 邀请关系实体类
 */
@Data
public class InviteRelaResp {

    private int id;

    private int uid;

    private int inviter_id;

    private String createdate;

    private User user;
}
