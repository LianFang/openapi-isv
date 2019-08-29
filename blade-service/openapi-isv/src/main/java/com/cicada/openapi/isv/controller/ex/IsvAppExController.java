package com.cicada.openapi.isv.controller.ex;

import com.cicada.openapi.isv.service.impl.ex.IsvAppExServiceImpl;
import com.cicada.openapi.isv.vo.IsvAppVO;
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
 * @createTime：2019-08-28
 */
@RestController
@RequestMapping("/isvapp-ex")
@AllArgsConstructor
@Api("isvapp 扩展，主要用来为isv分配clientId,clientSecret")
public class IsvAppExController extends BladeController {

	private IsvAppExServiceImpl isvAppExService;

	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "创建IsvApp", notes = "传入IsvAppVO")
	public R save(@Valid @RequestBody IsvAppVO isvAppVO) {
		return R.status(isvAppExService.create(isvAppVO));
	}

}
