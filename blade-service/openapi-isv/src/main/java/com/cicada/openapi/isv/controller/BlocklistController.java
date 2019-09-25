package com.cicada.openapi.isv.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.vo.IsvInfoVO;
import com.cicada.openapi.isv.wrapper.IsvInfoWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * isv 黑名单 管理
 *
 * @version 1.0
 * @author：xy
 * @createTime：2019-09-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/blocklist")
@Api(value = "isv黑名单管理", tags = "isv黑名单管理")
public class BlocklistController {

	private IIsvInfoService isvInfoService;

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "分页")
	public R<IPage<IsvInfoVO>> page(IsvInfo isvInfo, Query query) {
		LambdaQueryWrapper<IsvInfo> wrapper = Wrappers.<IsvInfo>query().lambda();
		if (!StringUtils.isEmpty(isvInfo.getName())) {
			wrapper.like(IsvInfo::getName, isvInfo.getName());
		}
		if (!StringUtils.isEmpty(isvInfo.getIsvNo())) {
			wrapper.eq(IsvInfo::getIsvNo, isvInfo.getIsvNo());
		}
		wrapper.eq(IsvInfo::getIsBlocked, 1);
		wrapper.eq(IsvInfo::getIsDeleted, 0);
		wrapper.orderByDesc(IsvInfo::getUpdateTime);
		IPage<IsvInfo> pages = isvInfoService.page(Condition.getPage(query), wrapper);
		return R.data(IsvInfoWrapper.build().pageVO(pages));
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "新增")
	public R save(@ApiParam(value = "isvNo", required = true) @RequestParam String isvNo) {
		return R.status(isvInfoService.addBlockList(isvNo));
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "删除")
	public R remove(@ApiParam(value = "isvNo", required = true) @RequestParam String isvNo) {
		return R.status(isvInfoService.removeBlockList(isvNo));
	}

	/**
	 * 查询不在黑名单的ISV列表
	 */
	@GetMapping("/queryIsvNotInBlackList")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "查询不在黑名单的ISV列表")
	public R<List<IsvInfoVO>> queryIsvNotInBlackList(IsvInfo isvInfo) {
		isvInfo.setIsBlocked(0);
		isvInfo.setIsDeleted(0);
		List<IsvInfo> isvInfoList = isvInfoService.list(Condition.getQueryWrapper(isvInfo));
		return R.data(IsvInfoWrapper.build().listVO(isvInfoList));
	}
}
