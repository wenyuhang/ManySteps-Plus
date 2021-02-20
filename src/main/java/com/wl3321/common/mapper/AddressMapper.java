package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.Address;
import com.wl3321.pojo.request.AddressReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:21
 * desc   : 收货地址
 */
@Mapper
@Repository
public interface AddressMapper {

    int add(AddressReq address);

    int deleteByID(int id);

    int updataByUid(AddressReq address);

    Address selectByUid(int uid);

}
