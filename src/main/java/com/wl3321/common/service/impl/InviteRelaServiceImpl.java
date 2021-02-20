package com.wl3321.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.wl3321.common.mapper.InviteRelaMapper;
import com.wl3321.common.service.InviteRelaService;
import com.wl3321.common.service.RedisService;
import com.wl3321.pojo.entity.InviteRela;
import com.wl3321.pojo.request.PageIDReq;
import com.wl3321.pojo.response.InviteRelaResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:30
 * desc   :
 */
@Service
public class InviteRelaServiceImpl implements InviteRelaService {

    @Autowired
    RedisService redisService;
    @Autowired
    InviteRelaMapper inviteRelaMapper;

    /**
     * 插入数据
     *
     * @param inviteRela
     * @return
     */
    @Override
    public int add(InviteRela inviteRela) {
        int code = inviteRelaMapper.add(inviteRela);
        //插入成功 清空该用户数据缓存
        if (code != 0) {
            //redis-key
            String key = inviteRelaKey + ":" + inviteRela.getId();
            Set<String> keys = redisService.keys(key + "*");
            redisService.del(keys);
        }
        return code;
    }

    /**
     * 根据邀请id查询
     *
     * @param req
     * @return
     */
    @Override
    public List<InviteRelaResp> selectByInviteID(PageIDReq req) {
        //redis-key
        int inviteId = Integer.parseInt(req.getId());
        String key = inviteRelaKey + ":" + inviteId + ":" + req.getPage() + ":" + req.getSize();
        List<InviteRelaResp> list = (List<InviteRelaResp>) redisService.get(key);
        if (list == null) {
            PageHelper.startPage(req.getPage(), req.getSize());
            list = inviteRelaMapper.selectByInviteID(inviteId);
            PageHelper.clearPage();
            redisService.set(key, list);
        }
        return list;
    }
}
