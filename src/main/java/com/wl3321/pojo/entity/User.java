package com.wl3321.pojo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2020/8/17 15:15
 * desc   : 用户实体类
 */
@Data
public class User {

    private int id;

    private String name;

    private String headimgurl;

    private String openid;

    @JsonIgnore
    private String unionid;

    @JsonIgnore
    private String session_key;

    @JsonIgnore
    private String access_token;

    private String account_status;

    private String phone;

    private int steps_total;

    private float coin_total;

    private int invite_total;

    private String createdate;

}
