package com.wl3321.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wl3321.common.mapper.StepsRecordMapper;
import com.wl3321.common.service.RedisService;
import com.wl3321.common.service.StepsRecordService;
import com.wl3321.pojo.entity.StepsRecord;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.request.PageReq;
import com.wl3321.pojo.response.StepsMonitorResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:36
 * desc   :
 */
@Service
public class StepsRecordServiceImpl implements StepsRecordService {

    @Autowired
    StepsRecordMapper stepsRecordMapper;
    @Autowired
    RedisService redisService;

    /**
     * 清除缓存
     */
    @Override
    public void clearCach(int uid) {
        Set<String> keys = redisService.keys(StepsRecordService.stepsRecordKey + ":" + uid + "*");
        redisService.del(keys);
    }

    /**
     * 插入数据
     *
     * @param stepsRecord
     * @return
     */
    @Override
    public int add(StepsRecord stepsRecord) {
        return stepsRecordMapper.add(stepsRecord);
    }

    /**
     * 更新数据
     *
     * @param stepsRecord
     * @return
     */
    @Override
    public int updateByID(StepsRecord stepsRecord) {
        return stepsRecordMapper.updateByID(stepsRecord);
    }

    /**
     * 根据用户id和运动日期查询
     *
     * @param uid
     * @param rundate
     * @return
     */
    @Override
    public StepsRecord selectByUidAndRundate(int uid, String rundate) {
        String key = stepsRecordKey + ":" + uid + ":" + rundate;
        StepsRecord stepsRecord = (StepsRecord) redisService.get(key);
        if (null == stepsRecord) {
            stepsRecord = stepsRecordMapper.selectByUidAndRundate(uid, rundate);
            redisService.set(key, stepsRecord, 1000 * 3600 * 24);
        }
        return stepsRecord;
    }

    /**
     * 根据用户id查询步数记录
     *
     * @param req
     * @return
     */
    @Override
    public PageInfo selectByUidDesc(PageIDReq req) {
        //redis-key
        int uid = Integer.parseInt(req.getId());
        String key = stepsRecordKey + ":" + uid + ":" + req.getPage() + ":" + req.getSize();
        PageInfo pageInfo = (PageInfo) redisService.get(key);
        if (null == pageInfo) {
            PageHelper.startPage(req.getPage(), req.getSize());
            List<StepsRecord> list = stepsRecordMapper.selectByUidDesc(uid);
            pageInfo = new PageInfo(list);
            redisService.set(key, pageInfo);
        }
        return pageInfo;
    }

    /**
     * 查询刷步监控记录 ######此查询为耗时查询######
     *
     * @return
     */
    @Override
    public PageInfo selectStepsMonitor(PageReq req) {
        String key = stepsMonitorKey + ":" + req.getPage() + ":" + req.getSize();
        PageInfo pageInfo = (PageInfo) redisService.get(key);
        if (null == pageInfo) {
            PageHelper.startPage(req.getPage(), req.getSize());
            List<StepsMonitorResp> list = stepsRecordMapper.selectStepsMonitor();
            pageInfo = new PageInfo(list);
//            redisService.set(key,pageInfo,1000*3600*6);
        }
        return pageInfo;
    }
}
