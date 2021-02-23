package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.StepsRecord;
import com.wl3321.pojo.response.StepsMonitorResp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:36
 * desc   : 步数记录
 */
@Mapper
@Repository
public interface StepsRecordMapper {

    int add(StepsRecord stepsRecord);

    int updateByID(StepsRecord stepsRecord);

    StepsRecord selectByUidAndRundate(int uid, String rundate);

    List<StepsRecord> selectByUidDesc(int uid);

    List<StepsMonitorResp> selectStepsMonitor();
}
