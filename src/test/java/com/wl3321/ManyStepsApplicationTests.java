package com.wl3321;

import com.wl3321.common.mapper.InviteRelaMapper;
import com.wl3321.common.service.*;
import com.wl3321.pojo.entity.*;
import com.wl3321.pojo.response.InviteRelaResp;
import com.wl3321.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Iterator;
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
//        User user = userService.selectByUserId(224);
//        UserRankInfo userRankInfo = new UserRankInfo();
//        userRankInfo.setId(user.getId());
//        userRankInfo.setName(user.getName());
//        userRankInfo.setHeadimgurl(user.getHeadimgurl());
//        userRankInfo.setOpenid(user.getOpenid());
        String rankKey = StepsRecordService.stepsRankKey + ":20210222" ;
//        System.out.println("===>"+redisService.zReverseRank(rankKey, userRankInfo));
//        String inviteRankKey = InviteRelaService.inviteRankKey;
//        System.out.println(redisService.zIncrby(inviteRankKey, userRankInfo, 1));
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisService.zReverseRangeByScoreWithScores(rankKey, 0, 10);
        System.out.println(typedTuples);


        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();
        while(iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            System.out.println("value:"+next.getValue()+" score:"+next.getScore());
		/*
			value:d score:-1.0
			value:a score:1.0
			value:c score:2.0
			value:b score:3.0
		 */
        }
    }

}
