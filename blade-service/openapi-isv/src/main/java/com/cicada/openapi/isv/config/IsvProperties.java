package com.cicada.openapi.isv.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @version 1.0
 * @author：xy
 * @createTime：2019-09-24
 */


@Data
@ConfigurationProperties(prefix = "isv")
public class IsvProperties {
	private long defaultUserId;
	/**
	 * 数据中心id
	 */
	private long dcId;
	/**
	 * 机器id
	 * <p>
	 * <p>
	 * 注意，相同服务，mId不能相同
	 */
	private long mId;
}
