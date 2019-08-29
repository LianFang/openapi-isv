package com.cicada.openapi.isv.service.impl.ex;

import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.vo.IsvInfoVO;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.DateTimeUtil;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.feign.IUserExClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @author：xy
 * @createTime：2019-08-27
 */
@Component
@Transactional
public class IsvInfoExServiceImpl{
	@Autowired
	private IIsvInfoService isvInfoService;

//	@Autowired
//	private IRoleClient roleClient;
	@Autowired
	private IUserExClient userExClient;


	/**
	 *
	 * @return
	 */
	public boolean create(IsvInfoVO isvInfoVO){

		User user = BeanUtil.copyWithConvert(isvInfoVO, User.class);
		IsvInfo isvInfo = BeanUtil.copyWithConvert(isvInfoVO, IsvInfo.class);

//		Role role = new Role();
//		role.setRoleName(user.getAccount());
//		role.setRoleAlias(user.getAccount());
//
//		R<Role> roleR = roleClient.create(role);
//		if(!roleR.isSuccess()){
//			throw new IllegalStateException("error create role: " + roleR.getMsg());
//		}
//		Role roleData = roleR.getData();
//
//		user.setRoleId(String.valueOf(roleData.getId().longValue()));

		user.setRealName(user.getName());


		User fill = fillData(user);

		R<User> userR = userExClient.create(fill);
		if(!userR.isSuccess()){
			throw new IllegalStateException("error create user: " + userR.getMsg());
		}
		User userData = userR.getData();


		isvInfo.setUserId(userData.getId());

		isvInfoService.save(fillData(isvInfo));

		return true;
	}

	private <T> T fillData(T t){
		if(t instanceof User){
			((User) t).setCreateDept(Long.parseLong(SecureUtil.getDeptId()));
			((User) t).setCreateTime(DateTimeUtil.toDate(LocalDateTime.now()));
			((User) t).setUpdateTime(DateTimeUtil.toDate(LocalDateTime.now()));
			((User) t).setCreateUser(SecureUtil.getUserId());
			((User) t).setUpdateUser(SecureUtil.getUserId());
			((User) t).setTenantId(SecureUtil.getTenantId());
		} else if(t instanceof IsvInfo){
			((IsvInfo) t).setCreateTime(LocalDateTime.now());
			((IsvInfo) t).setUpdateTime(LocalDateTime.now());
			((IsvInfo) t).setCreateUser(SecureUtil.getUserId());
			((IsvInfo) t).setUpdateUser(SecureUtil.getUserId());
		}else{
			throw new IllegalArgumentException("unknown type to convert");
		}

		return t;
	}

}
