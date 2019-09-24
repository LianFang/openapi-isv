package com.cicada.openapi.isv.service.impl.ex;

import cn.eduplus.bluewhale.client.domain.req.agent.AgentCommReq;
import cn.eduplus.bluewhale.client.domain.req.agent.AgentProductRelationRequestJS;
import cn.eduplus.bluewhale.client.domain.req.agent.AgentRoleRequestJS;
import cn.eduplus.bluewhale.client.domain.req.agent.AgentUserRequestJS;
import cn.eduplus.bluewhale.client.enums.ProductEnum;
import cn.eduplus.bluewhale.client.rpc.api.agent.IAgentRpcApi;
import cn.thinkjoy.uc.service.business.IUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cicada.openapi.isv.config.IsvProperties;
import com.cicada.openapi.isv.entity.IsvInfo;
import com.cicada.openapi.isv.service.IIsvInfoService;
import com.cicada.openapi.isv.vo.IsvInfoVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.utils.IdUtil;
import org.springblade.common.utils.SmsCodeUtil;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.DateTimeUtil;
import org.springblade.system.entity.Role;
import org.springblade.system.feign.IRoleClient;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.feign.IUserExClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

	@Autowired
	private IRoleClient roleClient;
	@Autowired
	private IUserExClient userExClient;
	@Reference
	private IUserService ucUserService;
	@Reference
	private IAgentRpcApi agentRpcApi;

	@Autowired
	private IsvProperties isvProperties;

//	@Autowired
//	private IBwAgentClient bwAgentClient;


	/**
	 *
	 * @return
	 */
	public boolean create(IsvInfoVO isvInfoVO){

		User user = BeanUtil.copyWithConvert(isvInfoVO, User.class);
		user.setAccount(isvInfoVO.getPhone());
		String password = SmsCodeUtil.generateNumCode(6);
		user.setPassword(password);
		IsvInfo isvInfo = BeanUtil.copyWithConvert(isvInfoVO, IsvInfo.class);

		//isv管理暂时没有account和password的概念，默认使用phone字段，既当account又当password
		Map<String, Object> condition = Maps.newHashMap();
		condition.put("phone", isvInfo.getPhone());
		condition.put("is_deleted", 0);
		int count = isvInfoService.count(Wrappers.<IsvInfo>query().allEq(condition));

		if (count > 0) {
			throw new IllegalStateException("phone已经存在");
		}
		long userIdByPhone = ucUserService.getUserIdByPhone(isvInfo.getPhone());
		if (userIdByPhone > 0) {
			throw new IllegalStateException("phone已经存在");
		}

		String isvNo = String.valueOf(new IdUtil(isvProperties.getDcId(), isvProperties.getMId()).nextId());

		saveAgent(isvInfo, isvNo, password);

		Role roleData = saveRole(user);

		user.setRoleId(String.valueOf(roleData.getId().longValue()));
		User userData = saveUser(user);

		isvInfo.setUserId(userData.getId());
		isvInfo.setIsvNo(isvNo);

		isvInfoService.save(fillData(isvInfo));

		return true;
	}

	private User saveUser(User user) {
		User fill = fillData(user);
		user.setRealName(user.getName());
		R<User> userR = userExClient.create(fill);
		if (!userR.isSuccess()) {
			throw new IllegalStateException("error create user: " + userR.getMsg());
		}
		return userR.getData();
	}

	private Role saveRole(User user) {
		Role role = new Role();
		role.setRoleName(user.getAccount());
		role.setRoleAlias(user.getAccount());

		R<Role> roleR = roleClient.create(role);
		if (!roleR.isSuccess()) {
			throw new IllegalStateException("error create role: " + roleR.getMsg());
		}
		return roleR.getData();
	}

	private void saveAgent(IsvInfo isvInfo, String isvNo, String password) {
		AgentCommReq agentReq = new AgentCommReq();
		agentReq.setProvinceCode(isvInfo.getProvinceCode());
		agentReq.setProvinceName(isvInfo.getProvinceName());
		agentReq.setCityCode(isvInfo.getCityCode());
		agentReq.setCityName(isvInfo.getCityName());
		agentReq.setAddress(isvInfo.getAddress());
		agentReq.setAgentName(isvInfo.getName());

		agentReq.setAgentNo(isvNo);
		agentReq.setContactUser(isvInfo.getContact());
		agentReq.setContactTel(isvInfo.getPhone());
		agentReq.setContactEmail(isvInfo.getEmail());
		agentReq.setMerchantManager(String.valueOf(isvProperties.getDefaultUserId()));
		agentReq.setBusinessOwner(String.valueOf(isvProperties.getDefaultUserId()));
		// agent标示为2，2代表该agent为isv
		agentReq.setMarked(2);
		AgentProductRelationRequestJS product = new AgentProductRelationRequestJS();
		product.setAgentNo(isvNo);
		product.setProductName(ProductEnum.SYS.getDesc());
		product.setProductCode(ProductEnum.SYS.getCode());
		product.setBusinessOwner(String.valueOf(isvProperties.getDefaultUserId()));
		List<AgentProductRelationRequestJS> agentProductList = Lists.newArrayList();
		agentProductList.add(product);
		agentReq.setAgentProductRelationList(agentProductList);

		AgentUserRequestJS agentUser = new AgentUserRequestJS();
		agentUser.setMobile(isvInfo.getPhone());
		agentUser.setUserName(isvInfo.getContact());

		List<AgentUserRequestJS> agentUserList = Lists.newArrayList();
		List<AgentRoleRequestJS> agentRoleList = Lists.newArrayList();
		agentUserList.add(agentUser);
		agentUser.setAgentRoleRequestJSList(agentRoleList);
		agentReq.setAgentUserRequestList(agentUserList);

		AgentRoleRequestJS agentRoleRequestJS = new AgentRoleRequestJS();
		agentRoleRequestJS.setRoleId(1L);
		agentRoleRequestJS.setProductCode(ProductEnum.SYS.getCode());
		agentRoleList.add(agentRoleRequestJS);

		agentUser.setAgentRoleRequestJSList(agentRoleList);

		//服务商性质，C代表公司，P代表个人
		//TODO:硬编码
		agentReq.setAttribute("C");
		agentReq.setOriginal("8");
		agentRpcApi.addAgent(agentReq, password);
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
