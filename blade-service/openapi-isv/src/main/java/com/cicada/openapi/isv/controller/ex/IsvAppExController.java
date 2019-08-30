package com.cicada.openapi.isv.controller.ex;

import com.cicada.openapi.isv.service.impl.ex.IsvAppExServiceImpl;
import com.cicada.openapi.isv.vo.IsvAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springblade.system.entity.AuthClient;
import org.springblade.system.feign.IAuthClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @version 1.0
 * @author：xy
 * @createTime：2019-08-28
 */
@RestController
@RequestMapping("/isvapp-ex")
@AllArgsConstructor
@Api(value = "isvapp 扩展，主要用来为isv分配clientId,clientSecret", tags = "为isv分配clientId,clientSecret，目前isv+tp业务中创建isv的应用必须调用该路径下的save接口")
public class IsvAppExController extends BladeController {

	private IsvAppExServiceImpl isvAppExService;
	private IAuthClient authClientService;

	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "创建IsvApp", notes = "传入IsvAppVO")
	public R save(@Valid @RequestBody IsvAppVO isvAppVO) {
		return R.status(isvAppExService.create(isvAppVO));
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情")
	public R<AuthClient> detail(AuthClient authApp) {
		R<AuthClient> detail = authClientService.detailOnPost(authApp);
		if (detail.isSuccess())
			return R.data(detail.getData());
		else
			return R.fail(detail.getCode(), detail.getMsg());
	}

}
