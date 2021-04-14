package com.wl3321.pojo.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/4/14 11:06
 * desc   : 版本审核
 */
@Getter
@Setter
public class VersionReview {
    private int id;

    private int version;

    private String desc;

    private Timestamp create_time;

    private Timestamp update_time;
}
