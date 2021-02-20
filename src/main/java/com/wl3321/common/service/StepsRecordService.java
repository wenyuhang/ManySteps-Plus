package com.wl3321.common.service;

import com.wl3321.pojo.entity.StepsRecord;
import com.wl3321.pojo.request.PageIDReq;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:36
 * desc   :
 */
public interface StepsRecordService {

    String stepsRecordKey = "stepsRecord";
    String stepsRankKey = "stepsRank";

    int add(StepsRecord stepsRecord);

    int updateByID(StepsRecord stepsRecord);

    StepsRecord selectByUidAndRundate(int uid,String rundate);

    List<StepsRecord> selectByUidDesc(PageIDReq req);
}
