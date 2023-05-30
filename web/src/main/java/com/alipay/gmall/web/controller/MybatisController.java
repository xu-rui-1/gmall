/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.web.controller;

import com.alipay.gmall.dal.dao.ProductBaseInfoDAO;
import com.alipay.gmall.dal.domain.ProductBaseInfoDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ruitu.xr
 * @version MybatisController.java, v 0.1 2023年05月30日 13:27 ruitu.xr Exp $
 */
@RestController
@Api(tags = "Mybatis测试controller")
public class MybatisController {
    @Resource
    public ProductBaseInfoDAO productBaseInfoDAO;

    @GetMapping("/queryProductById")
    @ApiOperation("根据id查询产品信息")
    public ProductBaseInfoDO queryProductById(Integer id) {
        return productBaseInfoDAO.selectByPrimaryKey(id);
    }
}
