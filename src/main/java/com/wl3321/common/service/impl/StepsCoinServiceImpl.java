package com.wl3321.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.wl3321.common.mapper.StepsCoinMapper;
import com.wl3321.common.service.*;
import com.wl3321.pojo.entity.StepsCoin;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:34
 * desc   :
 */
@Service
public class StepsCoinServiceImpl implements StepsCoinService {

    @Autowired
    StepsCoinMapper stepsCoinMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

    /**
     * 发放奖励
     * @param user                  用户
     * @param tran_desc             奖励描述
     * @param coin                  金币数
     * @return
     */
    @Override
    public int add(User user,String tran_desc,float coin) {
        StepsCoin stepsCoin = new StepsCoin();
        stepsCoin.setUid(user.getId());
        stepsCoin.setTran_desc(tran_desc);
        stepsCoin.setCoin(coin);
        stepsCoin.setRundate(format.format(System.currentTimeMillis()));
        stepsCoin.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        int code = stepsCoinMapper.add(stepsCoin);
        if (code != 0){
            //计算金币总和
            user.setCoin_total(user.getCoin_total()+coin);
            user.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
            userService.update(user);
            //清除缓存
            clearCach(user.getId());
        }
        return code;
    }

    /**
     * 根据uid查询交易记录
     * @param req
     * @return
     */
    @Override
    public List<StepsCoin> selectByUidDateDesc(PageIDReq req) {
        //redis-key
        int uid = Integer.parseInt(req.getId());
        String key = stepsCoinKey + ":" + uid + ":" + req.getPage() + ":" + req.getSize();
        List<StepsCoin> list = (List<StepsCoin>) redisService.get(key);
        if (list == null){
            PageHelper.startPage(req.getPage(),req.getSize());
            list = stepsCoinMapper.selectByUidDateDesc(uid);
            PageHelper.clearPage();
            redisService.set(key,list);
        }
        return list;
    }

    /**
     * 清除缓存
     */
    private void clearCach(int uid) {
        Set<String> keys = redisService.keys(stepsCoinKey +":"+uid+ "*");
        redisService.del(keys);
    }
}
