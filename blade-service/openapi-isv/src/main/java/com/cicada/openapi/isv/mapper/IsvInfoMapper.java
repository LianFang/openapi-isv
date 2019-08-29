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
package com.cicada.openapi.isv.mapper;

import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.vo.IsvInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的 Mapper 接口
 *
 * @author BladeX
 * @since 2019-08-15
 */
public interface IsvInfoMapper extends BaseMapper<IsvInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param isvInfo
	 * @return
	 */
	List<IsvInfoVO> selectIsvInfoPage(IPage page, IsvInfoVO isvInfo);

}
