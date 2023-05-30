/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.service.impl;

import com.alipay.gmall.common.enums.ResultCodeEnum;
import com.alipay.gmall.common.exception.ProductBizException;
import com.alipay.gmall.dal.dao.ProductBaseInfoDAO;
import com.alipay.gmall.dal.domain.ProductBaseInfoDO;
import com.alipay.gmall.dal.domain.ProductBaseInfoParam;
import com.alipay.gmall.dto.ProductBaseInfoDTO;
import com.alipay.gmall.service.ProductMngService;
import com.alipay.gmall.utils.ProductMngServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ruitu.xr
 * @version ProductMngServiceImpl.java, v 0.1 2023年05月30日 13:40 ruitu.xr Exp $
 */
@Service
public class ProductMngServiceImpl implements ProductMngService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProductMngServiceImpl.class);

    @Resource
    private ProductBaseInfoDAO productBaseInfoDAO;

    @Override
    public int addProduct(ProductBaseInfoDTO productBaseInfoDTO) {
        ProductBaseInfoDO productBaseInfoDO = ProductMngServiceUtil.convertProductBaseInfoDTO2DO(productBaseInfoDTO);
        int ret = productBaseInfoDAO.insertSelective(productBaseInfoDO);
        if (ret <= 0) {
            String msg = "由于网络抖动原因，插入产品信息失败，请联系技术同学....";
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.INSERT_ERROR, msg);
        }
        return productBaseInfoDO.getId();
    }

    @Override
    public int updateProduct(ProductBaseInfoDTO productBaseInfoDTO) {
        ProductBaseInfoDO productBaseInfoDO = ProductMngServiceUtil.convertProductBaseInfoDTO2DO(productBaseInfoDTO);
        int ret = productBaseInfoDAO.updateByPrimaryKeySelective(productBaseInfoDO);
        if (ret <= 0) {
            String msg = "更新产品信息失败，请联系技术同学....";
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.UPDATE_ERROR, msg);
        }

        return productBaseInfoDO.getId();
    }

    @Override
    public void deleteProductById(int id) {
        int ret = productBaseInfoDAO.deleteByPrimaryKey(id);
        if (ret <= 0) {
            String msg = String.format("删除产品失败, 产品id=%s", id);
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.DELETE_ERROR, msg);
        }
    }

    @Override
    public List<ProductBaseInfoDTO> queryProductInfoByName(String productName) {
        ProductBaseInfoParam param = new ProductBaseInfoParam();
        param.createCriteria().andProductNameEqualTo(productName);
        List<ProductBaseInfoDO> productBaseInfoDOS = productBaseInfoDAO.selectByParam(param);
        if (CollectionUtils.isEmpty(productBaseInfoDOS)) {
            String msg = String.format("不存在该名称的产品, productName=%s", productName);
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.QUERY_ERROR, msg);
        }

        List<ProductBaseInfoDTO> productBaseInfoDTOS = new ArrayList<>();
        productBaseInfoDOS.forEach(s -> {
            ProductBaseInfoDTO productBaseInfoDTO = ProductMngServiceUtil.convertProductBaseInfoDO2DTO(s);
            productBaseInfoDTOS.add(productBaseInfoDTO);

        });
        return productBaseInfoDTOS;
    }

    @Override
    public ProductBaseInfoDTO queryProductInfoById(Integer id) {
        ProductBaseInfoDO productBaseInfoDO = productBaseInfoDAO.selectByPrimaryKey(id);
        if (Objects.isNull(productBaseInfoDO)) {
            String msg = String.format("不存在id=%s的商品", id);
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.QUERY_ERROR, msg);
        }
        return ProductMngServiceUtil.convertProductBaseInfoDO2DTO(productBaseInfoDO);
    }

    @Override
    public List<ProductBaseInfoDTO> queryAllProduct(int pageIndex, int pageSize) {
        ProductBaseInfoParam param = new ProductBaseInfoParam();
        param.setPagination(pageIndex, pageSize);
        param.appendOrderByClause(ProductBaseInfoParam.OrderCondition.ID, ProductBaseInfoParam.SortType.DESC);
        List<ProductBaseInfoDO> productBaseInfoDOS = productBaseInfoDAO.selectByParam(param);
        if (CollectionUtils.isEmpty(productBaseInfoDOS)) {
            String msg = "当前不存在商品";
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.QUERY_ERROR, msg);
        }

        List<ProductBaseInfoDTO> productBaseInfoDTOS = new ArrayList<>();
        productBaseInfoDOS.forEach(s -> {
            ProductBaseInfoDTO productBaseInfoDTO = ProductMngServiceUtil.convertProductBaseInfoDO2DTO(s);
            productBaseInfoDTOS.add(productBaseInfoDTO);

        });
        return productBaseInfoDTOS;
    }
}
