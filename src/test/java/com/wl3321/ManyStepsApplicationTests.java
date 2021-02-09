package com.wl3321;

import com.wl3321.common.service.ProductService;
import com.wl3321.common.service.RedisService;
import com.wl3321.pojo.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@SpringBootTest
class ManyStepsApplicationTests {

    @Autowired
    RedisService redisService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ProductService productService;

    @Test
    void contextLoads() {
        Product product = productService.getByName("水烟斗1");
        System.out.println(product);
    }

}
