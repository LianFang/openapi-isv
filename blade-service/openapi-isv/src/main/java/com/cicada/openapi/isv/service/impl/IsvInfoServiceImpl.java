/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.cicada.openapi.isv.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.mapper.IsvInfoMapper;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.vo.IsvInfoVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的 服务实现类
 *
 * @author BladeX
 * @since 2019-08-15
 */
@Service
public class IsvInfoServiceImpl extends ServiceImpl<IsvInfoMapper, IsvInfo> implements IIsvInfoService {

	@Override
	public IPage<IsvInfoVO> selectIsvInfoPage(IPage<IsvInfoVO> page, IsvInfoVO isvInfo) {
		return page.setRecords(baseMapper.selectIsvInfoPage(page, isvInfo));
	}

	private IsvInfo queryByIsvNo(String isvNo) {
		Map<String, Object> condition = Maps.newHashMap();
		condition.put("isv_no", isvNo);
		condition.put("is_deleted", 0);
		return this.getOne(Wrappers.<IsvInfo>query().allEq(condition));
	}

	@Override
	public boolean addBlockList(String isvNo) {
		if (StringUtils.isBlank(isvNo)) {
			throw new IllegalArgumentException("参数不合法");
		}
		IsvInfo isvInfo = this.queryByIsvNo(isvNo);
		if (isvInfo == null) {
			throw new IllegalArgumentException("ISV不存在");
		}
		if (isvInfo.getIsBlocked() == 1) {
			throw new IllegalArgumentException("ISV已经加入黑名单");
		}
		isvInfo.setIsBlocked(1);
		isvInfo.setUpdateTime(LocalDateTime.now());
		return this.updateById(isvInfo);
	}

	@Override
	public boolean removeBlockList(String isvNo) {
		if (StringUtils.isBlank(isvNo)) {
			throw new IllegalArgumentException("参数不合法");
		}
		IsvInfo isvInfo = this.queryByIsvNo(isvNo);
		if (isvInfo == null) {
			throw new IllegalArgumentException("ISV不存在");
		}
		if (isvInfo.getIsBlocked() == 0) {
			throw new IllegalArgumentException("ISV不在黑名单");
		}
		isvInfo.setIsBlocked(0);
		isvInfo.setUpdateTime(LocalDateTime.now());
		return this.updateById(isvInfo);
	}
}
