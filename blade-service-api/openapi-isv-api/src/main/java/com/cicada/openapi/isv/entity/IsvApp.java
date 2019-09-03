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
 * isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在
 该表中保存对应关系实体类
 *
 * @author BladeX
 * @since 2019-09-02
 */
@Data
@TableName("t_isv_app")
@ApiModel(value = "IsvApp对象", description = "isv和client关系表，原始设计中client是由用户创建的，开放平台中扩展为isv可以创建app，而app即client，app创建成功以后会在 该表中保存对应关系")
public class IsvApp implements Serializable {
	private Long id;
	/**
	* 应用client表的id
	*/
		@ApiModelProperty(value = "应用client表的id")
		private Long cId;
	/**
	* 应用名称
	*/
		@ApiModelProperty(value = "应用名称")
		private String name;
	/**
	* 应用描述
	*/
		@ApiModelProperty(value = "应用描述")
		private String remark;
	/**
	* 属于哪个isv
	*/
		@ApiModelProperty(value = "属于哪个isv")
		private Long isvUserId;
	private Long createUser;
	private LocalDateTime createTime;
	private Long updateUser;
	private LocalDateTime updateTime;
	private Integer status;
	private Integer isDeleted;
	/**
	 * 应用id
	 */
	@ApiModelProperty(value = "应用id")
	@TableField("appId")
	private String appId;
	/**
	 * 密钥
	 */
	@ApiModelProperty(value = "密钥")
	@TableField("appSecret")
	private String appSecret;


}
