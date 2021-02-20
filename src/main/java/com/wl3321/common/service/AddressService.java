package com.wl3321.common.service;

import com.wl3321.pojo.entity.Address;
import com.wl3321.pojo.request.AddressReq;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:26
 * desc   :
 */
public interface AddressService {
    String addressKey = "address";

    int add(AddressReq address);

    int deleteByUid(int uid);

    int updataByUid(AddressReq address);

    Address selectByUid(int uid);
}
