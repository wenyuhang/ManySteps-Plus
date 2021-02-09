package com.wl3321.common.service;

import com.wl3321.pojo.entity.Product;
import com.wl3321.pojo.request.PageBeanReq;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/2 10:36
 * desc   :
 */
public interface ProductService {

    String productKey = "product";

    int add(Product product);

    int delete(int id);

    int update(Product product);

    Product getById(int id);

    Product getByName(String name);

    List<Product> selectByCoinASC(PageBeanReq req);
}
