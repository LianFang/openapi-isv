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
import java.util.Date;

/**
 * isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的视图实体类
 *
 * @author BladeX
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "IsvVO对象", description = "isv信息登记表")
public class IsvInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	@ApiModelProperty(value = "isv的联系人", required = true)
	private String contact;
	/**
	 * isv的联系电话
	 */
	@ApiModelProperty(value = "isv的联系人电话联系电话", required = true)
	private String phone;
	/**
	 * isv名称，公司名称
	 */
	@ApiModelProperty(value = "isv名称，公司名称", required = true)
	private String name;
	/**
	 * 营业执照扫副本描件，url
	 */
	@ApiModelProperty(value = "营业执照扫副本描件，url", required = true)
	private String businessLicense;
	/**
	 * isv地址
	 */
	@ApiModelProperty(value = "isv地址", required = true)
	private String address;

	@ApiModelProperty(value = "isv账号，在换取token时使用，以后也会在公网开放登陆", required = true)
	private String account;
	@ApiModelProperty(value = "isv密码，在换取token时使用，以后也会在公网开放登陆", required = true)
	private String password;
	@ApiModelProperty(value = "isv联系人头像")
	private String avatar;
	@ApiModelProperty(value = "isv邮箱", required = true)
	private String email;
	@ApiModelProperty(value = "isv联系人出生年月")
	private Date birthday;
	@ApiModelProperty(value = "isv联系人性别,1:男,0:女,默认男")
	private Integer sex = 1;



}
