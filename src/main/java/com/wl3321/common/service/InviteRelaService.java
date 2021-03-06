package com.wl3321.common.service;

import com.github.pagehelper.PageInfo;
import com.wl3321.pojo.entity.InviteRela;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.response.InviteRelaResp;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:29
 * desc   :
 */
public interface InviteRelaService {
    String inviteRelaKey = "inviteRela";
    String inviteRankKey = "inviteRank";

    int add(InviteRela inviteRela);

    PageInfo selectByInviteID(PageIDReq req);
}
