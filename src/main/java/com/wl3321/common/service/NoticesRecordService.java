package com.wl3321.common.service;

import com.wl3321.pojo.entity.NoticesRecord;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.PageReq;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 18:42
 * desc   :
 */
public interface NoticesRecordService {

    String noticesRecordKey = "noticesRecord:uid";
    String noticesRecordListKey = "noticesRecord:list:";

    void clearUserCach(int uid);

    int add(User user, String desc, int steps, float coin);

    int updateById(NoticesRecord noticesRecord);

    boolean selectHaveNotice(int uid);

    NoticesRecord selectUserNotice(int uid);

    List<NoticesRecord> selectByDateDesc(PageReq req);
}
