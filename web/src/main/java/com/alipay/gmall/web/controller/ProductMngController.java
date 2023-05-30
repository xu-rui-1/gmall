/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.web.controller;

import com.alipay.gmall.dto.ProductBaseInfoDTO;
import com.alipay.gmall.service.ProductMngService;
import com.alipay.gmall.web.utils.ProductBaseInfoConvertUtil;
import com.alipay.gmall.web.vo.ProductBaseInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ruitu.xr
 * @version ProductMngController.java, v 0.1 2023年05月30日 21:05 ruitu.xr Exp $
 */
@RestController
@Api(tags = "产品管理接口")
public class ProductMngController {

    public static final Logger LOGGER  = LoggerFactory.getLogger(ProductMngController.class);

    @Resource
    private ProductMngService productMngService;

    @PostMapping("/addProduct")
    @ApiOperation("添加产品")
    public int addProduct(@RequestBody ProductBaseInfoVO productBaseInfoVO) {
        ProductBaseInfoDTO productBaseInfoDTO = ProductBaseInfoConvertUtil.convertProductBaseInfoVO2DTO(productBaseInfoVO);
        Integer ret = productMngService.addProduct(productBaseInfoDTO);
        if (ret < 0) {
            LOGGER.error(String.format("Add product error, productBaseInfoVO=%s", productBaseInfoVO));
        }
        return ret;
    }

    @PostMapping("/updateProduct")
    @ApiOperation("添加产品")
    public int updateProduct(@RequestBody ProductBaseInfoVO productBaseInfoVO) {
        ProductBaseInfoDTO productBaseInfoDTO = ProductBaseInfoConvertUtil.convertProductBaseInfoVO2DTO(productBaseInfoVO);
        Integer ret = productMngService.updateProduct(productBaseInfoDTO);
        if (ret < 0) {
            LOGGER.error(String.format("Add product error, productBaseInfoVO=%s", productBaseInfoVO));
        }
        return ret;
    }
}
