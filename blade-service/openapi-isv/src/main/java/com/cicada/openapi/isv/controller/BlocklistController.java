package com.cicada.openapi.isv.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * isv 黑名单 管理
 *
 * @version 1.0
 * @author：xy
 * @createTime：2019-09-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/blocklist")
@Api(value = "isv黑名单管理", tags = "isv黑名单管理")
public class BlocklistController {

}
