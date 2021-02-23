package com.wl3321.pojo.response;

import com.wl3321.pojo.entity.UserRankInfo;
import lombok.Data;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/22 13:50
 * desc   : 排行榜
 */

public class RankListResp {
    private Set<ZSetOperations.TypedTuple<Object>> rankList;
    private int userRanking;
    private List<UserRankInfoResp> rankData;

    public List<UserRankInfoResp> getRankList() {
        rankData = new ArrayList<>();
        Iterator<ZSetOperations.TypedTuple<Object>> iterator2 = rankList.iterator();
        while(iterator2.hasNext()){
            ZSetOperations.TypedTuple<Object> next = iterator2.next();
            UserRankInfo user = (UserRankInfo) next.getValue();
            double score = next.getScore();
            UserRankInfoResp userRankInfoResp = new UserRankInfoResp();
            userRankInfoResp.setId(user.getId());
            userRankInfoResp.setName(user.getName());
            userRankInfoResp.setHeadimgurl(user.getHeadimgurl());
            userRankInfoResp.setOpenid(user.getOpenid());
            userRankInfoResp.setSteps_total((int)score);
            userRankInfoResp.setInvite_total((int)score);
            rankData.add(userRankInfoResp);
        }
        return rankData;
    }

    public void setRankList(Set<ZSetOperations.TypedTuple<Object>> rankList) {
        this.rankList = rankList;
    }

    public int getUserRanking() {
        return userRanking;
    }

    public void setUserRanking(int userRanking) {
        this.userRanking = userRanking;
    }
}
