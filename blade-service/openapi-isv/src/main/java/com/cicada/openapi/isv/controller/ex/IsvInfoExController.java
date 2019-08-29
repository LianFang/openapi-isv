package com.cicada.openapi.isv.controller.ex;

import com.cicada.openapi.isv.service.impl.ex.IsvInfoExServiceImpl;
import com.cicada.openapi.isv.vo.IsvInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @version 1.0
 * @author：xy
 * @createTime：2019-08-27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/isvinfo-ex")
@Api(value = "isv扩展api，主要用来进行复杂业务的处理")
public class IsvInfoExController extends BladeController {

	private IsvInfoExServiceImpl isvInfoExService;

	/**
	 * 新增 isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
	 该表和user表是一一对应的
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "创建isv", notes = "传入isvInfoVO")
	public R save(@Valid @RequestBody IsvInfoVO isvInfoVO) {
		return R.status(isvInfoExService.create(isvInfoVO));
	}

}
