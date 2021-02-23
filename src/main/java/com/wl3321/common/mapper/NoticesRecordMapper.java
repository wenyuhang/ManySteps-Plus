package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.NoticesRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;


/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:21
 * desc   : 系统公告
 */
@Mapper
@Repository
public interface NoticesRecordMapper {


    int add(NoticesRecord noticesRecord);

    int updateById(NoticesRecord noticesRecord);

    NoticesRecord selectByUidAndDate(int uid, Timestamp create_time);

    List<NoticesRecord> selectByDateDesc();

}
