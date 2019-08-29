package com.cicada.openapi.isv.service.impl.ex;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cicada.openapi.isv.entity.IsvApp;
import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.service.IIsvAppService;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.vo.IsvAppVO;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.system.entity.AuthClient;
import org.springblade.system.feign.IAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
		/**
		 * 在isvapp表中有clientId字段表示client表中的主键
		 * 而在client表中也存在clientId字段，表示的是应用的
		 * 名称。这点特殊情况需要特殊处理。
		 *
		 */
		//处理clientkey，使之成为clientId
		clientApp.setClientId(isvAppVO.getClientKey());
		R<AuthClient> authClientR = authClient.create(clientApp);

		if(!authClientR.isSuccess()){
			throw new IllegalStateException("error create authclient, error msg:" + authClientR.getMsg());
		}

		AuthClient data = authClientR.getData();

		isvApp.setClientId(data.getId());

		int count = isvInfoService.count(Wrappers.<IsvInfo>query().eq("user_id", isvApp.getIsvUserId().longValue()));

		if(count < 0){
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
}
