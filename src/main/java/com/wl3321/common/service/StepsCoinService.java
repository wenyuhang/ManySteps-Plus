package com.wl3321.common.service;

import com.github.pagehelper.PageInfo;
import com.wl3321.pojo.entity.StepsCoin;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.PageIDReq;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:34
 * desc   :
 */
public interface StepsCoinService {
    String stepsCoinKey = "stepsCoin";

    int add(User user, String tran_desc, float coin);

    PageInfo selectByUidDateDesc(PageIDReq req);
}
