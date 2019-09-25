package com.cicada.openapi.isv.controller.ex;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.service.impl.ex.IsvInfoExServiceImpl;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @version 1.0
 * @author：xy
 * @createTime：2019-08-27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/isvinfo-ex")
@Api(value = "isv管理", tags = "isv管理")
public class IsvInfoExController extends BladeController {

	private IsvInfoExServiceImpl isvInfoExService;
	private IIsvInfoService isvInfoService;

	/**
	 * 新增 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
	 该表和user表是一一对应的
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "创建isv")
	public R save(@Valid @RequestBody IsvInfoVO isvInfoVO) {
		return R.status(isvInfoExService.create(isvInfoVO));
	}


	/**
	 * 新增 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
	 * 该表和user表是一一对应的
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "修改isv")
	public R update(@Valid @RequestBody IsvInfoVO isvInfoVO) {
		return R.status(isvInfoExService.update(isvInfoVO));
	}

	/**
	 * 分页 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
	 * 该表和user表是一一对应的
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询isv列表分页")
	public R<IPage<IsvInfoVO>> page(@ApiParam("isv名称") @RequestParam(required = false) String name,
									@ApiParam("isvId") @RequestParam(required = false) Long id,
									Query query) {
		IsvInfo isvInfo = new IsvInfo();
		LambdaQueryWrapper<IsvInfo> like = Wrappers.<IsvInfo>query().lambda();
		if (name != null) {
			isvInfo.setName(name);
			like.like(IsvInfo::getName, name);
		}
		if (id != null) {
			isvInfo.setId(id);
			like.like(IsvInfo::getId, id);
		}

		IPage<IsvInfo> pages = isvInfoService.page(Condition.getPage(query), like);
		return R.data(IsvInfoWrapper.build().pageVO(pages));
	}

	/**
	 * 查询isv列表
	 */
	@GetMapping("/listAll")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询isv列表")
	public R<List<IsvInfoVO>> listAll(@ApiParam("isv名称") @RequestParam(required = false) String name) {
		LambdaQueryWrapper<IsvInfo> wrapper = Wrappers.<IsvInfo>query().lambda();
		if (name != null) {
			wrapper.like(IsvInfo::getName, name);
		}
		wrapper.eq(IsvInfo::getIsDeleted, 0);
		List<IsvInfo> isvInfoList = isvInfoService.list(wrapper);
		return R.data(IsvInfoWrapper.build().listVO(isvInfoList));
	}

}
