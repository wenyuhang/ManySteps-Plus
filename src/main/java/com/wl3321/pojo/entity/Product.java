package com.wl3321.pojo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/2 10:29
 * desc   : 商品实体类
 */
@Data
public class Product {

    private int id;

    private String name;

    private float coin;

    private float price;

    private int stock;

    private String subTitle;

    private String imageurl;

    private float energy;

    private String createdate;

}
