package com.wl3321.pojo.entity;

import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/19 17:00
 * desc   : 步数记录实体类
 */
@Data
public class StepsRecord {

    private int id;

    private int uid;

    private int steps;

    private String rundate;

    private int convertedsteps;

    private String createdate;

}
