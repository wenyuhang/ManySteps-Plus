package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/1/18 18:04
 * desc   : 商品
 */
@Mapper
@Repository
public interface ProductMapper {

    int add(Product product);

    int deleteById(int id);

    int updateById(Product product);

    Product getById(int id);

    Product getByName(String name);

    List<Product> selectByCoinASC();

}
