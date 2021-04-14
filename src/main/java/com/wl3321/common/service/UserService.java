package com.wl3321.common.service;

import com.github.pagehelper.PageInfo;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.PageReq;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/1 17:53
 * desc   :
 */
public interface UserService {
    String userKey = "user";

    int add(User user);

    User selectByUserId(int id);

    User selectByOpenid(String openid);

    PageInfo selectByDateDesc(PageReq req);

    int update(User user);

    List<User> selectAll();
}
