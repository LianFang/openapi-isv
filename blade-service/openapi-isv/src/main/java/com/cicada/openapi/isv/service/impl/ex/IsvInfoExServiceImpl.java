package com.cicada.openapi.isv.service.impl.ex;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.vo.IsvInfoVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Map;

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

		//轻量级tp中没有account和password的概念，默认使用phone字段，既当account又当password
		if (StringUtils.isBlank(user.getAccount())) {
			user.setAccount(isvInfo.getPhone());
		}
		if (StringUtils.isBlank(user.getPassword())) {
			user.setPassword(isvInfo.getPhone());
		}

		User fill = fillData(user);

		R<User> userR = userExClient.create(fill);
		if(!userR.isSuccess()){
			throw new IllegalStateException("error create user: " + userR.getMsg());
		}
		User userData = userR.getData();


		isvInfo.setUserId(userData.getId());

		Map<String, Object> condition = Maps.newHashMap();
		condition.put("phone", isvInfo.getPhone());
		condition.put("is_deleted", 0);
		condition.put("status", 0);
		int count = isvInfoService.count(Wrappers.<IsvInfo>query().allEq(condition));

		if (count > 0) {
			throw new IllegalStateException("phone已经存在");
		}

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

	/**
	 * 更新isv
	 * 如果isv的email或者phone字段有更新，则需要同步更新user表
	 * 否则只需要更新isvinfo
	 *
	 * @param isvInfoVO
	 * @return
	 */
	public boolean update(IsvInfoVO isvInfoVO) {
		IsvInfo isvInfo = BeanUtil.copyWithConvert(isvInfoVO, IsvInfo.class);
		Long userId = isvInfoVO.getUserId();
		if (userId == null) {
			throw new IllegalArgumentException("error to update, becaues it's userId is null");
		}
		//需要更新user表的email
		if (StringUtils.isNotBlank(isvInfo.getEmail()) || StringUtils.isNotBlank(isvInfo.getPhone())) {

			R<User> userById = userExClient.getUserById(userId);
			if (userById.isSuccess()) {
				User user = userById.getData();

				if (StringUtils.isNotBlank(isvInfo.getEmail())) {
					user.setEmail(isvInfo.getEmail());
				}
				//轻量级tp中没有account和password的概念，默认使用phone字段，既当account又当password
				//更新的时候把账户和密码也更新掉
				if (StringUtils.isNotBlank(isvInfo.getPhone())) {
					user.setAccount(isvInfo.getPhone());
					user.setPassword(isvInfo.getPhone());
				}
				user.setUpdateTime(DateTimeUtil.toDate(LocalDateTime.now()));
				userExClient.update(user);
			} else {
				return false;
			}
		}

		isvInfoService.updateById(isvInfo);

		return true;

	}

}
