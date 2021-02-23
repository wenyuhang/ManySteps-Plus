package com.wl3321.common.service;

import com.wl3321.pojo.entity.Product;
import com.wl3321.pojo.request.PageReq;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/2 10:36
 * desc   :
 */
public interface ProductService {

    String productKey = "product";

    void clearCach();

    int add(Product product);

    int delete(int id);

    int update(Product product);

    Product selectById(int id);

    Product selectByName(String name);

    List<Product> selectByCoinASC(PageReq req);
}
