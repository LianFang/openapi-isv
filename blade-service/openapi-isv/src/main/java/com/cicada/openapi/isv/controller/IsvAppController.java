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
import com.cicada.openapi.isv.entity.IsvApp;
import com.cicada.openapi.isv.service.IIsvAppService;
import com.cicada.openapi.isv.service.impl.ex.IsvAppExServiceImpl;
import com.cicada.openapi.isv.vo.IsvAppVO;
import com.cicada.openapi.isv.wrapper.IsvAppWrapper;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 *
 * @author BladeX
 * @since 2019-08-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/isvapp")
@Api(value = "应用管理", tags = "应用管理")
public class IsvAppController extends BladeController {

	private IIsvAppService isvAppService;
	private IsvAppExServiceImpl isvAppExService;

	/**
	* 详情
	*/
	@ApiIgnore
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查询详情")
	public R<IsvAppVO> detail(IsvApp isvApp) {
		IsvApp detail = isvAppService.getOne(Condition.getQueryWrapper(isvApp));
		return R.data(IsvAppWrapper.build().entityVO(detail));
	}

	/**
	* 分页 isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
该表中保存对应关系
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取应用列表分页")
	public R<IPage<IsvAppVO>> list(IsvApp isvApp,
								   Query query) {

		IPage<IsvApp> pages = isvAppService.page(Condition.getPage(query), Condition.getQueryWrapper(isvApp));
		R<IPage<IsvAppVO>> data = R.data(IsvAppWrapper.build().pageVO(pages));
		return data;
	}

	/**
	* 自定义分页 isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
该表中保存对应关系
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入isvApp")
	public R<IPage<IsvAppVO>> page(IsvAppVO isvApp, Query query) {
		IPage<IsvAppVO> pages = isvAppService.selectIsvAppPage(Condition.getPage(query), isvApp);
		return R.data(pages);
	}

	/**
	* 新增 isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
该表中保存对应关系
	*/
	@PostMapping("/save")
	@ApiIgnore
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入isvApp")
	public R save(@Valid @RequestBody IsvApp isvApp) {
		return R.status(isvAppService.save(isvApp));
	}

	/**
	* 修改 isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
该表中保存对应关系
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改应用")
	public R update(@Valid @RequestBody IsvApp isvApp) {
		return R.status(isvAppService.updateById(isvApp));
	}

	/**
	* 新增或修改 isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
该表中保存对应关系
	*/
	@PostMapping("/submit")
	@ApiIgnore
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入isvApp")
	public R submit(@Valid @RequestBody IsvApp isvApp) {
		return R.status(isvAppService.saveOrUpdate(isvApp));
	}


	/**
	* 删除 isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
该表中保存对应关系
	*/
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiIgnore
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(isvAppService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 删除 isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
	 * 该表中保存对应关系
	 */
	@PostMapping("/delete")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除应用", notes = "支持批量和单个删除，批量传入ids")
	public R delete(@ApiParam(value = "支持批量和单个删除，如果有多个id，把id拼接起来中间使用逗号，即','隔开", required = true) @RequestParam String ids) {
		return R.status(isvAppService.removeByIds(Func.toLongList(ids)));
	}


	@PostMapping("/create")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "创建应用")
	public R create(@Valid @RequestBody IsvAppVO isvAppVO) {
		return R.status(isvAppExService.create(isvAppVO));
	}


}
