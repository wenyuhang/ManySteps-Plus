package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/1/18 18:04
 * desc   : 用户
 */
@Mapper
@Repository
public interface UserMapper {

    int add(User user);

    int updateById(User user);

    User selectByUserId(int id);

    User selectByOpenid(String openid);

    List<User> selectByDateDesc();

}
