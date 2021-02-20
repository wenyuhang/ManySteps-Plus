package com.wl3321.pojo.entity;

import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 16:35
 * desc   : 金币明细实体类
 */
@Data
public class StepsCoin {

    private int id;

    private int uid;

    private String tran_desc;

    private float coin;

    private String rundate;

    private String createdate;

}
