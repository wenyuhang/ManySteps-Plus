package com.wl3321.pojo.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 18:12
 * desc   : 公告系统
 */
@Data
public class NoticesRecord {

    private int id;

    private int uid;

    private boolean p_status;

    private String p_desc;

    private int steps;

    private float coin;

    private Timestamp create_time;

    private Timestamp update_time;

}
