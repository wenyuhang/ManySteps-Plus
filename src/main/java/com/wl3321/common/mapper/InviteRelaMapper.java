package com.wl3321.common.mapper;

import com.wl3321.pojo.entity.InviteRela;
import com.wl3321.pojo.response.InviteRelaResp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/18 11:29
 * desc   : 邀请关系
 */
@Mapper
@Repository
public interface InviteRelaMapper {

    int add(InviteRela inviteRela);

    List<InviteRelaResp> selectByInviteID(int inviteId);
}
