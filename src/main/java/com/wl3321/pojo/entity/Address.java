package com.wl3321.pojo.entity;

import lombok.Data;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/19 13:43
 * desc   :
 */
@Data
public class Address {
    private int id;

    private int uid;

    private String receiver;

    private String address;

    private String mobile;

    private String province;

    private String city;

    private String area;

    private String post;

    private String createdate;
}
