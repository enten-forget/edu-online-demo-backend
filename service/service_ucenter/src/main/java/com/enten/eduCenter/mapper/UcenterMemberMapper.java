package com.enten.eduCenter.mapper;

import com.enten.eduCenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author 遠天
 * @since 2021-08-27
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    /**
     * 查询某一天注册人数
     */
    Integer countRegister(String day);
}
