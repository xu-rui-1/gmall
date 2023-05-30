/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ruitu.xr
 * @version Swagger2Controller.java, v 0.1 2023年05月30日 12:21 ruitu.xr Exp $
 */
@RestController
@Api(tags = "Swagger2测试")
@RequestMapping("/swagger2")
public class Swagger2Controller {
    @GetMapping("/swagger2")
    @ApiOperation(value = "测试swagger")
    public String test() {
        return "swagger2";
    }
}
