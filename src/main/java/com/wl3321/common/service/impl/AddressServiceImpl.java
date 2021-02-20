package com.wl3321.common.service.impl;

import com.wl3321.common.mapper.AddressMapper;
import com.wl3321.common.service.AddressService;
import com.wl3321.common.service.RedisService;
import com.wl3321.pojo.entity.Address;
import com.wl3321.pojo.request.AddressReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:26
 * desc   :
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressMapper addressMapper;
    @Autowired
    RedisService redisService;

    /**
     * 插入数据
     * @param address
     * @return
     */
    @Override
    public int add(AddressReq address) {
        return addressMapper.add(address);
    }

    /**
     * 删除数据
     * @param uid
     * @return
     */
    @Override
    public int deleteByID(int uid) {
        return addressMapper.deleteByID(uid);
    }

    /**
     * 更新数据
     * @param address
     * @return
     */
    @Override
    public int updataByUid(AddressReq address) {
        return addressMapper.updataByUid(address);
    }

    /**
     * 根据uid查询数据
     * @param uid
     * @return
     */
    @Override
    public Address selectByUid(int uid) {
        //redis-key
        String key = addressKey+":uid:"+uid;
        Address address = (Address) redisService.get(key);
        if (address == null){
            address = addressMapper.selectByUid(uid);
            redisService.set(key,address);
        }
        return address;
    }


}
