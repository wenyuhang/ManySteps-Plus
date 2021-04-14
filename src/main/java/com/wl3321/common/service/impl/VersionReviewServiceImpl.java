package com.wl3321.common.service.impl;

import com.wl3321.common.mapper.VersionReviewMapper;
import com.wl3321.common.service.VersionReviewService;
import com.wl3321.pojo.entity.VersionReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/4/14 11:08
 * desc   :
 */
@Service
public class VersionReviewServiceImpl implements VersionReviewService {

    @Autowired
    VersionReviewMapper versionReviewMapper;


    /**
     * 获取审核版本
     * @return
     */
    @Override
    public VersionReview list() {
        List<VersionReview> list = versionReviewMapper.selectByTimeDesc();
        if (list!=null && list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }
}
