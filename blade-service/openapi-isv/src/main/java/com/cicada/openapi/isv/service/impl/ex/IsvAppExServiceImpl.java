package com.cicada.openapi.isv.service.impl.ex;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cicada.openapi.isv.entity.IsvApp;
import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.service.IIsvAppService;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.vo.IsvAppVO;
import com.google.common.collect.Maps;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.system.entity.AuthClient;
import org.springblade.system.feign.IAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @version 1.0
 * @author：xy
 * @createTime：2019-08-28
 */
@Service
@Transactional
public class IsvAppExServiceImpl {

	@Autowired
	private IAuthClient authClient;
	@Autowired
	private IIsvAppService isvAppService;

	@Autowired
	private IIsvInfoService isvInfoService;

	public boolean create(IsvAppVO isvAppVO) {
		IsvApp isvApp = BeanUtil.copyWithConvert(isvAppVO, IsvApp.class);
		AuthClient clientApp = BeanUtil.copyWithConvert(isvAppVO, AuthClient.class);
		clientApp.setClientId(isvAppVO.getAppId());
		clientApp.setClientSecret(isvAppVO.getAppSecret());
		R<AuthClient> authClientR = authClient.create(clientApp);

		if(!authClientR.isSuccess()){
			throw new IllegalStateException("error create authclient, error msg:" + authClientR.getMsg());
		}

		AuthClient data = authClientR.getData();

		isvApp.setCId(data.getId());
		Map<String, Object> condition = Maps.newHashMap();
		condition.put("user_id", isvApp.getIsvUserId().longValue());
		condition.put("is_deleted", 0);
		condition.put("status", 0);

		int count = isvInfoService.count(Wrappers.<IsvInfo>query().allEq(condition));

		if (count < 1) {
			throw new IllegalArgumentException("isv user_id is invalid: " + isvApp.getIsvUserId().longValue());
		}

		isvAppService.save(fillIsvApp(isvApp));

		return true;

	}

	private IsvApp fillIsvApp(IsvApp isvApp){

		isvApp.setUpdateTime(LocalDateTime.now());
		isvApp.setCreateUser(SecureUtil.getUserId());
		isvApp.setUpdateUser(SecureUtil.getUserId());
		isvApp.setCreateTime(LocalDateTime.now());

		return isvApp;
	}

	private boolean delete(String ids) {
		return false;
	}

	private boolean update(IsvApp isvApp) {


		return false;
	}
}
