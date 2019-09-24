/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.cicada.openapi.isv.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
该表中保存对应关系视图实体类
 *
 * @author BladeX
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode()
@ApiModel(value = "IsvAppVO对象")
public class IsvAppVO implements Serializable {
	private Long id;

	/**
	 * 应用名称
	 */
	@ApiModelProperty(value = "应用名称。创建时必填项，其他选填")
	private String name;
	/**
	 * 属于哪个isv
	 */
	@ApiModelProperty(value = "属于哪个isv,创建时必填，修改时选填")
	private Long isvUserId;


	/**
	 * 客户端id，client表的clientId字段
	 * 注意与上边的clientId区别
	 */
	@ApiModelProperty(value = "客户端id。创建时必填项，其他选填")
	private String appId;
	/**
	 * 客户端密钥
	 */
	@ApiModelProperty(value = "客户端密钥。创建时必填项，其他选填")
	private String appSecret;

	/**
	 * 应用client表的id
	 */
	@ApiModelProperty(value = "clientId，修改时必填项，其他选填")
	private Long cId;

	/**
	 * 应用描述
	 */
	@ApiModelProperty(value = "应用描述")
	private String remark;
	/**
	 * 资源集合
	 */
	@ApiModelProperty(value = "资源集合")
	private String resourceIds;
	/**
	 * 授权范围
	 */
	@ApiModelProperty(value = "授权范围")
	private String scope = "all";
	/**
	 * 授权类型
	 */
	@ApiModelProperty(value = "授权类型")
	private String authorizedGrantTypes = "refresh_token,password,authorization_code";
	/**
	 * 回调地址
	 */
	@ApiModelProperty(value = "回调地址")
	private String webServerRedirectUri;
	/**
	 * 权限
	 */
	@ApiModelProperty(value = "权限")
	private String authorities;
	/**
	 * 令牌过期秒数
	 */
	@ApiModelProperty(value = "令牌过期秒数")
	private Integer accessTokenValidity = 3600;
	/**
	 * 刷新令牌过期秒数
	 */
	@ApiModelProperty(value = "刷新令牌过期秒数")
	private Integer refreshTokenValidity = 60480;
	/**
	 * 附加说明
	 */
	@ApiModelProperty(value = "附加说明")
	private String additionalInformation;
	/**
	 * 自动授权
	 */
	@ApiModelProperty(value = "自动授权")
	private String autoapprove;


	private Long createUser;
	private LocalDateTime createTime;
	private Long updateUser;
	private LocalDateTime updateTime;
	private Integer status;
	private Integer isDeleted;

}
