package com.cicada.openapi.isv.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @author：xy
 * @createTime：2019-08-16
 */

@Configuration
@MapperScan("com.cicada.**.mapper.**")
public class CustomizeMybatisConfiguration {
}
