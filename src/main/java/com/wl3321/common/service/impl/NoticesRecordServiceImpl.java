package com.wl3321.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.wl3321.common.mapper.NoticesRecordMapper;
import com.wl3321.common.service.NoticesRecordService;
import com.wl3321.common.service.RedisService;
import com.wl3321.common.service.StepsCoinService;
import com.wl3321.pojo.entity.NoticesRecord;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.PageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 18:42
 * desc   :
 */
@Service
public class NoticesRecordServiceImpl implements NoticesRecordService {

    @Autowired
    RedisService redisService;
    @Autowired
    NoticesRecordMapper noticesRecordMapper;
    @Autowired
    StepsCoinService stepsCoinService;

    /**
     * 清除分页缓存
     */
    public void clearListCach() {
        Set<String> keys = redisService.keys(noticesRecordListKey + "*");
        redisService.del(keys);
    }

    /**
     * 清除用户公告
     * @param uid
     */
    @Override
    public void clearUserCach(int uid) {
        String key = noticesRecordKey+uid;
        redisService.del(key);
    }

    /**
     * 增加公告
     * @param user            用户
     * @param desc            公告描述
     * @param steps           处罚步数（重置为多少步）
     * @param coin            交易金额
     * @return
     */
    @Override
    public int add(User user,String desc,int steps,float coin) {
        int uid = user.getId();
        //创建公告实体
        NoticesRecord noticesRecord = new NoticesRecord();
        noticesRecord.setCoin(coin);
        noticesRecord.setSteps(steps);
        noticesRecord.setP_desc(desc);
        noticesRecord.setUid(uid);
        noticesRecord.setP_status(false);
        noticesRecord.setCreate_time(new Timestamp(System.currentTimeMillis()));
        noticesRecord.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        //插入数据库
        int code = noticesRecordMapper.add(noticesRecord);
        if (code != 0){
            noticesRecord = noticesRecordMapper.selectByUidAndDate(uid, noticesRecord.getCreate_time());
            //加入缓存
            String redisKey = noticesRecordKey+uid;
            redisService.set(redisKey,noticesRecord);
            //清除列表缓存
            clearListCach();
        }
        return code;
    }

    /**
     * 更新公告信息
     * @param noticesRecord
     * @return
     */
    @Override
    public int updateById(NoticesRecord noticesRecord) {
        return noticesRecordMapper.updateById(noticesRecord);
    }

    /**
     * 查询用户是否有未读公告
     * @param uid
     * @return
     */
    @Override
    public boolean selectHaveNotice(int uid) {
        String redisKey = noticesRecordKey+uid;
        NoticesRecord redisValue = (NoticesRecord) redisService.get(redisKey);
        return redisValue == null;
    }

    /**
     * 查询用户的未读公告
     * @param uid
     * @return
     */
    @Override
    public NoticesRecord selectUserNotice(int uid) {
        String redisKey = noticesRecordKey+uid;
        NoticesRecord redisValue = (NoticesRecord) redisService.get(redisKey);
        return redisValue;
    }

    /**
     * 根据时间分页倒序查询全部公告
     * @param req
     * @return
     */
    @Override
    public PageInfo selectByDateDesc(PageReq req) {
        //redis-key
        String redisKey = noticesRecordListKey+req.getPage()+":"+req.getSize();
        PageInfo pageInfo = (PageInfo) redisService.get(redisKey);
        if (null == pageInfo){
            PageHelper.startPage(req.getPage(),req.getSize());
            List<NoticesRecord> list = noticesRecordMapper.selectByDateDesc();
            pageInfo = new PageInfo(list);
            redisService.set(redisKey,pageInfo);
        }
        return pageInfo;
    }
}
