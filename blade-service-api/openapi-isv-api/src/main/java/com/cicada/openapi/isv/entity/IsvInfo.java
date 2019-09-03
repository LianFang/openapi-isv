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
package com.cicada.openapi.isv.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。
该表和user表是一一对应的实体类
 *
 * @author BladeX
 * @since 2019-09-02
 */
@Data
@TableName("t_isv_info")
@ApiModel(value = "IsvInfo对象", description = "isv信息表，每个isv创建都会在先在user表里面生成该isv的登陆信息，然后在该表生成isv的额外信息。 该表和user表是一一对应的")
public class IsvInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
		@ApiModelProperty(value = "主键")
		private Long id;
	/**
	* 用户id，关联到blade_user表中
	*/
		@ApiModelProperty(value = "用户id，关联到blade_user表中")
		private Long userId;
	/**
	* isv的联系人
	*/
		@ApiModelProperty(value = "isv的联系人")
		private String contact;
	/**
	* isv的联系电话
	*/
		@ApiModelProperty(value = "isv的联系电话")
		private String phone;
	/**
	* isv名称，公司名称
	*/
		@ApiModelProperty(value = "isv名称，公司名称")
		private String name;
	/**
	* 营业执照扫副本描件，url
	*/
		@ApiModelProperty(value = "营业执照扫副本描件，url")
		private String businessLicense;
	/**
	* isv地址
	*/
		@ApiModelProperty(value = "isv地址")
		private String address;
	private Long createUser;
	private LocalDateTime createTime;
	private Long updateUser;
	private LocalDateTime updateTime;
	private Integer status;
	private Integer isDeleted;
	/**
	 * isv编号
	 */
	@ApiModelProperty(value = "isv编号")
	@TableField("isvNo")
	private String isvNo;
	/**
	 * isv联系邮箱
	 */
	@ApiModelProperty(value = "isv联系邮箱")
	private String email;


}
