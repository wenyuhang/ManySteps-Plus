package com.wl3321.common.service.impl;

import com.github.pagehelper.PageHelper;
import com.wl3321.common.mapper.ProductMapper;
import com.wl3321.common.service.AddressService;
import com.wl3321.common.service.ProductService;
import com.wl3321.common.service.RedisService;
import com.wl3321.pojo.entity.Product;
import com.wl3321.pojo.request.PageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/2 10:37
 * desc   :
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    RedisService redisService;


    /**
     * 清除缓存
     */
    @Override
    public void clearCach() {
        Set<String> keys = redisService.keys(ProductService.productKey + "*");
        redisService.del(keys);
    }

    @Override
    public int add(Product product) {
        int code = productMapper.add(product);
        return code;
    }


    @Override
    public int delete(int id) {
        int code = productMapper.deleteById(id);
        return code;
    }

    @Override
    public int update(Product product) {
        int code = productMapper.updateById(product);
        return code;
    }

    @Override
    public Product selectById(int id) {
        return productMapper.selectById(id);
    }

    @Override
    public Product selectByName(String name) {
        return productMapper.selectByName(name);
    }

    @Override
    public List<Product> selectByCoinASC(PageReq req) {
        //redis-key
        String key = productKey+":"+req.getPage()+":"+req.getSize();
        List<Product> list = (List<Product>) redisService.get(key);
        if (list == null){
            PageHelper.startPage(req.getPage(),req.getSize());
            list = productMapper.selectByCoinASC();
            PageHelper.clearPage();
            redisService.set(key,list);
        }
        return list;
    }

}
