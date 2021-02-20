package com.wl3321.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.wl3321.common.mapper.UserMapper;
import com.wl3321.common.service.RedisService;
import com.wl3321.common.service.UserService;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.PageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/1 17:53
 * desc   :
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RedisService redisService;
    @Autowired
    UserMapper userMapper;

    /**
     * 添加新用户
     * @param user
     * @return
     */
    @Override
    public int add(User user) {
        return userMapper.add(user);
    }

    /**
     * 根据userID查询
     * @param id
     * @return
     */
    @Override
    public User selectByUserId(int id) {
        //redis-key
        String key = userKey+":userid:"+id;
        User user = (User) redisService.get(key);
        if (user == null){
            user = userMapper.selectByUserId(id);
            redisService.set(key,user);
        }
        return user;
    }

    /**
     * 根据openid查询
     * @param openid
     * @return
     */
    @Override
    public User selectByOpenid(String openid) {
        //redis-key
        String key = userKey+":openid:"+openid;
        User user = (User) redisService.get(key);
        if (user == null){
            user = userMapper.selectByOpenid(openid);
            redisService.set(key,user);
        }
        return user;
    }

    /**
     * 根据创建时间倒序查询
     * @param req
     * @return
     */
    @Override
    public List<User> selectByDateDesc(PageReq req) {
        //redis-key
        String key = userKey+":"+req.getPage()+":"+req.getSize();
        List<User> list = (List<User>) redisService.get(key);
        if (list == null){
            PageHelper.startPage(req.getPage(),req.getSize());
            list = userMapper.selectByDateDesc();
            PageHelper.clearPage();
            redisService.set(key,list);
        }
        return list;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public int update(User user) {
        int code = userMapper.updateById(user);
        if (code != 0){
            //清除缓存
            clearCach(user.getId(),user.getOpenid());
        }
        return code;
    }

    /**
     * 清除缓存
     */
    private void clearCach(int uid,String openid) {
        Set<String> keyss = redisService.keys(userKey+":userid:"+uid+ "*");
        redisService.del(keyss);
        Set<String> keys = redisService.keys(userKey+":openid:"+openid+ "*");
        redisService.del(keys);

    }
}
