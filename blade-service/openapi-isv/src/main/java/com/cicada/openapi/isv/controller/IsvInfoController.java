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
package com.cicada.openapi.isv.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.vo.IsvInfoVO;
import com.cicada.openapi.isv.wrapper.IsvInfoWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的 控制器
 *
 * @author BladeX
 * @since 2019-08-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/isvinfo")
@Api(value = "isv基础api", tags = "isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。 该表和user表是一一对应的接口")
public class IsvInfoController extends BladeController {

	private IIsvInfoService isvInfoService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入isvInfo")
	public R<IsvInfoVO> detail(IsvInfo isvInfo) {
		IsvInfo detail = isvInfoService.getOne(Condition.getQueryWrapper(isvInfo));
		return R.data(IsvInfoWrapper.build().entityVO(detail));
	}

	/**
	* 分页 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入isvInfo")
	public R<IPage<IsvInfoVO>> list(IsvInfo isvInfo, Query query) {
		IPage<IsvInfo> pages = isvInfoService.page(Condition.getPage(query), Condition.getQueryWrapper(isvInfo));
		return R.data(IsvInfoWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入isvInfo")
	public R<IPage<IsvInfoVO>> page(IsvInfoVO isvInfo, Query query) {
		IPage<IsvInfoVO> pages = isvInfoService.selectIsvInfoPage(Condition.getPage(query), isvInfo);
		return R.data(pages);
	}

	/**
	* 新增 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入isvInfo")
	public R save(@Valid @RequestBody IsvInfo isvInfo) {
		return R.status(isvInfoService.save(isvInfo));
	}

	/**
	* 修改 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入isvInfo")
	public R update(@Valid @RequestBody IsvInfo isvInfo) {
		return R.status(isvInfoService.updateById(isvInfo));
	}

	/**
	* 新增或修改 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入isvInfo")
	public R submit(@Valid @RequestBody IsvInfo isvInfo) {
		return R.status(isvInfoService.saveOrUpdate(isvInfo));
	}


	/**
	* 删除 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(isvInfoService.removeByIds(Func.toLongList(ids)));
	}


}
