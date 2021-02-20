package com.wl3321;

import com.wl3321.common.mapper.InviteRelaMapper;
import com.wl3321.common.service.*;
import com.wl3321.pojo.entity.Address;
import com.wl3321.pojo.entity.InviteRela;
import com.wl3321.pojo.entity.Product;
import com.wl3321.pojo.entity.User;
import com.wl3321.pojo.response.InviteRelaResp;
import com.wl3321.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;

@SpringBootTest
class ManyStepsApplicationTests {

    @Autowired
    RedisService redisService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    InviteRelaService inviteRelaService;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
//        InviteRela inviteRela = new InviteRela();
//        inviteRela.setUid(224);
//        inviteRela.setInviter_id(1);
//        inviteRela.setCreatedate(DateUtils.stampToDate(System.currentTimeMillis()));
//        System.out.println(inviteRelaService.add(inviteRela));
//        List<InviteRelaResp> inviteRelaResps = inviteRelaMapper.selectByInviteID(1);
//        System.out.println(inviteRelaResps.size());
//        System.out.println(userService.selectByUserId(224));
        User user = userService.selectByUserId(224);
        user.setInvite_total(10);
        //步数排行榜
        String rankKey = StepsRecordService.stepsRankKey + ":20210220" ;
        redisService.zSet(rankKey, user, 9999);
    }

}
