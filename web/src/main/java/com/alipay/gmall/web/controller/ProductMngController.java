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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ruitu.xr
 * @version ProductMngController.java, v 0.1 2023年05月30日 21:05 ruitu.xr Exp $
 */
@RestController
@Api(tags = "产品管理接口")
@RequestMapping("/productmng")
public class ProductMngController {

    public static final Logger LOGGER  = LoggerFactory.getLogger(ProductMngController.class);

    @Resource
    private ProductMngService productMngService;

    @PostMapping("/addProduct")
    @ApiOperation("添加产品")
    public int addProduct(@RequestBody ProductBaseInfoVO productBaseInfoVO) {
        ProductBaseInfoDTO productBaseInfoDTO = ProductBaseInfoConvertUtil.convertProductBaseInfoVO2DTO(productBaseInfoVO);
        int ret = productMngService.addProduct(productBaseInfoDTO);
        if (ret <= 0) {
            LOGGER.error(String.format("Add product error, productBaseInfoVO=%s", productBaseInfoVO));
        }
        return ret;
    }

    @PostMapping("/updateProduct")
    @ApiOperation("更新产品")
    public int updateProduct(@RequestBody ProductBaseInfoVO productBaseInfoVO) {
        ProductBaseInfoDTO productBaseInfoDTO = ProductBaseInfoConvertUtil.convertProductBaseInfoVO2DTO(productBaseInfoVO);
        int ret = productMngService.updateProduct(productBaseInfoDTO);
        if (ret <= 0) {
            LOGGER.error(String.format("Add product error, productBaseInfoVO=%s", productBaseInfoVO));
        }
        return ret;
    }

    @GetMapping("/deleteProductById")
    @ApiOperation("根据id删除产品")
    public void deleteProductById(@RequestParam int id) {
        productMngService.deleteProductById(id);
    }

    @GetMapping("/queryProductInfoByName")
    @ApiOperation("根据名称查询产品")
    public List<ProductBaseInfoVO> queryProductInfoByName(@RequestParam String productName) {
        List<ProductBaseInfoDTO> productBaseInfoDTOS = productMngService.queryProductInfoByName(productName);
        List<ProductBaseInfoVO> productBaseInfoVOS = new ArrayList<>();
        productBaseInfoDTOS.forEach(s -> {
            ProductBaseInfoVO productBaseInfoVO = ProductBaseInfoConvertUtil.convertProductBaseInfoDTO2VO(s);
            productBaseInfoVOS.add(productBaseInfoVO);
        });

        return productBaseInfoVOS;
    }

    @GetMapping("/queryProductById")
    @ApiOperation("根据id查询产品")
    public ProductBaseInfoVO queryProductById(@RequestParam int id) {
        ProductBaseInfoDTO productBaseInfoDTO = productMngService.queryProductInfoById(id);
        return ProductBaseInfoConvertUtil.convertProductBaseInfoDTO2VO(productBaseInfoDTO);
    }

    @GetMapping("/queryAllProduct")
    @ApiOperation("分页查询全部产品")
    public List<ProductBaseInfoVO> queryAllProduct(@RequestParam (required = false, defaultValue = "1") int pageIndex,
                                                   @RequestParam (required = false, defaultValue = "10") int pageSize) {
        List<ProductBaseInfoDTO> productBaseInfoDTOS = productMngService.queryAllProduct(pageIndex, pageSize);
        List<ProductBaseInfoVO> productBaseInfoVOS = new ArrayList<>();
        productBaseInfoDTOS.forEach(s -> {
            ProductBaseInfoVO productBaseInfoVO = ProductBaseInfoConvertUtil.convertProductBaseInfoDTO2VO(s);
            productBaseInfoVOS.add(productBaseInfoVO);
        });

        return productBaseInfoVOS;
    }
}
