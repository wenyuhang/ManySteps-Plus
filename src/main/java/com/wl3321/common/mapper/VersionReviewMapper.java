package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.VersionReview;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/4/14 11:05
 * desc   :
 */
@Mapper
@Repository
public interface VersionReviewMapper {
    List<VersionReview> selectByTimeDesc();
}
