package com.wl3321.pojo.response;

import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 17:40
 * desc   : 监控违规刷步
 */
@Data
public class StepsMonitorResp {

    private int id;

    private int uid;

    private int steps;

    private String rundate;

    private int convertedsteps;

    private String createdate;

    private int COUNT;

//    private User user;
}
