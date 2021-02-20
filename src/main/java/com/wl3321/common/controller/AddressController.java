package com.wl3321.common.controller;

import com.wl3321.common.service.AddressService;
import com.wl3321.common.service.RedisService;
import com.wl3321.common.service.UserService;
import com.wl3321.pojo.ApiResponse;
import com.wl3321.pojo.entity.Address;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.request.AddressReq;
import com.wl3321.pojo.request.IDReq;
import com.wl3321.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/19 13:57
 * desc   : 收货地址管理
 */
@RestController
@RequestMapping(value = "/userinfo")
public class AddressController {

    @Autowired
    AddressService addressService;
    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;

    /**
     * 添加收货地址
     * @param req
     * @return
     */
    @RequestMapping(value = "/addAddress",method = RequestMethod.POST)
    public ApiResponse add(@Validated @RequestBody AddressReq req){
        //检查用户是否存在
        User user = userService.selectByUserId(req.getUid());
        if (null==user){
            return ApiResponse.of(999,"该用户不存在",null);
        }
        //检查该地址是否存在
        Address ads = addressService.selectByUid(req.getUid());
        if (null!=ads){
            return ApiResponse.of(999,"收货地址已存在，请勿多次添加",null);
        }
        //添加收货地址
        req.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        int code = addressService.add(req);
        if (code==0){
            return ApiResponse.of(999,"操作失败请重试",null);
        }
        return ApiResponse.ofSuccess(null);
    }

    /**
     * 删除收货地址
     * @param req
     * @return
     */
    @RequestMapping(value = "/deleteAddress",method = RequestMethod.POST)
    public ApiResponse delete(@Validated @RequestBody IDReq req){
        int code = addressService.deleteByID(Integer.parseInt(req.getId()));
        if (code==0){
            return ApiResponse.of(999,"操作失败请重试",null);
        }
        //清除redis 缓存
        clearCach(Integer.parseInt(req.getId()));
        return ApiResponse.ofSuccess(null);
    }

    /**
     * 修改地址
     * @param req
     * @return
     */
    @PostMapping(value = "/updateAddress")
    public ApiResponse update(@Validated @RequestBody AddressReq req){
        //检查该地址是否存在
        Address address = addressService.selectByUid(req.getUid());
        if (null==address){
            return ApiResponse.of(999,"参数错误请重试",null);
        }
        req.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
        int updata = addressService.updataByUid(req);
        if (updata==0){
            return ApiResponse.of(999,"操作失败请重试",null);
        }
        //清除redis 缓存
        clearCach(req.getUid());
        return ApiResponse.ofSuccess(null);
    }

    /**
     * 清除缓存
     */
    private void clearCach(int uid) {
        String key = AddressService.addressKey+":uid:"+uid;
        redisService.del(key);
    }

    /**
     * 获取收货地址
     * @param req
     * @return
     */
    @PostMapping(value = "/getAddress")
    public ApiResponse get(@Validated @RequestBody IDReq req){
        //检查该地址是否存在
        Address address = addressService.selectByUid(Integer.parseInt(req.getId()));
        if (null==address){
            return ApiResponse.of(999,"该用户没有添加收货地址",null);
        }
        return ApiResponse.ofSuccess(address);
    }
}
