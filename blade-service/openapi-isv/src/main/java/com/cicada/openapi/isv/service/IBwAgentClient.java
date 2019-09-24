package com.cicada.openapi.isv.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 调用蓝鲸服务商接口
 *
 * @version 1.0
 * @author：xy
 * @createTime：2019-09-20
 */
@FeignClient(name = "bwAgentClient", url = "${blade.prop.agentServiceUrl}")
public interface IBwAgentClient {

	@PostMapping("/agent/addAgent")
	String addAgent();
}
