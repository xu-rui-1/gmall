/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.web.controller;

import com.alipay.gmall.common.config.EnvConfig;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ruitu.xr
 * @version EnvConfigController.java, v 0.1 2023年05月30日 12:33 ruitu.xr Exp $
 */
@RestController
@Api(tags = "多环境配置测试controller")
@RequestMapping("/env")
public class EnvConfigController {
    @Resource
    private EnvConfig envConfig;

    @GetMapping("/queryEnv")
    public String queryEnv() {
        return envConfig.getTitle();
    }
}
