package com.wl3321.common.service;

import com.wl3321.pojo.entity.StepsRecord;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.request.PageReq;
import com.wl3321.pojo.response.StepsMonitorResp;

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
    String stepsTotalRankKey = "stepsTotalRank";
    String stepsMonitorKey = "stepsMonitor";

    void clearCach(int uid);

    int add(StepsRecord stepsRecord);

    int updateByID(StepsRecord stepsRecord);

    StepsRecord selectByUidAndRundate(int uid,String rundate);

    List<StepsRecord> selectByUidDesc(PageIDReq req);

    List<StepsMonitorResp> selectStepsMonitor(PageReq req);
}
