package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.StepsCoin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:34
 * desc   : 步数兑换金币
 */
@Mapper
@Repository
public interface StepsCoinMapper {

    int add(StepsCoin stepsCoin);

    List<StepsCoin> selectByUidDateDesc(int uid);
}
