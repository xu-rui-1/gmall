/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.service.impl;

import com.alipay.gmall.common.client.RedisClient;
import com.alipay.gmall.common.constant.RedisConstants;
import com.alipay.gmall.common.enums.ResultCodeEnum;
import com.alipay.gmall.common.exception.ProductBizException;
import com.alipay.gmall.dal.dao.ProductBaseInfoMapper;
import com.alipay.gmall.dal.domain.ProductBaseInfo;
import com.alipay.gmall.dal.domain.ProductBaseInfoExample;
import com.alipay.gmall.dto.ProductBaseInfoDTO;
import com.alipay.gmall.service.ProductMngService;
import com.alipay.gmall.utils.ProductMngServiceUtil;
import com.github.pagehelper.PageHelper;
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
@Service("productMngService")
public class ProductMngServiceImpl implements ProductMngService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProductMngServiceImpl.class);

    @Resource
    private ProductBaseInfoMapper productBaseInfoMapper;

    @Resource
    private RedisClient redisClient;

    @Override
    public int addProduct(ProductBaseInfoDTO productBaseInfoDTO) {
        ProductBaseInfo productBaseInfo = ProductMngServiceUtil.convertProductBaseInfoDTO2DO(productBaseInfoDTO);
        int ret = productBaseInfoMapper.insertSelectiveWithGeneratedKey(productBaseInfo);
        if (ret <= 0) {
            String msg = "由于网络抖动原因，插入产品信息失败，请联系技术同学....";
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.INSERT_ERROR, msg);
        }
        return productBaseInfo.getId();
    }

    @Override
    public int updateProduct(ProductBaseInfoDTO productBaseInfoDTO) {
        ProductBaseInfo productBaseInfo = ProductMngServiceUtil.convertProductBaseInfoDTO2DO(productBaseInfoDTO);
        int ret = productBaseInfoMapper.updateByPrimaryKeySelective(productBaseInfo);
        if (ret <= 0) {
            String msg = "更新产品信息失败，请联系技术同学....";
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.UPDATE_ERROR, msg);
        }

        return productBaseInfo.getId();
    }

    @Override
    public void deleteProductById(int id) {
        int ret = productBaseInfoMapper.deleteByPrimaryKey(id);
        if (ret <= 0) {
            String msg = String.format("删除产品失败, 产品id=%s", id);
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.DELETE_ERROR, msg);
        }
    }

    @Override
    public List<ProductBaseInfoDTO> queryProductInfoByName(String productName) {
        ProductBaseInfoExample example = new ProductBaseInfoExample();
        example.createCriteria().andProductNameEqualTo(productName);
        List<ProductBaseInfo> productBaseInfos = productBaseInfoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(productBaseInfos)) {
            String msg = String.format("不存在该名称的产品, productName=%s", productName);
            LOGGER.error(msg);
            throw new ProductBizException(ResultCodeEnum.QUERY_ERROR, msg);
        }

        List<ProductBaseInfoDTO> productBaseInfoDTOS = new ArrayList<>();
        productBaseInfos.forEach(s -> {
            ProductBaseInfoDTO productBaseInfoDTO = ProductMngServiceUtil.convertProductBaseInfoDO2DTO(s);
            productBaseInfoDTOS.add(productBaseInfoDTO);

        });
        return productBaseInfoDTOS;
    }

    @Override
    public ProductBaseInfoDTO queryProductInfoById(Integer id) {
        /**
         * 该接口并发量比较多，如果直接查数据库的话，可能会打挂数据库，故需要使用redis缓存
         * 使用双检加锁策略进行查询
         */
        String key = RedisConstants.PRODUCT_KEY_PREFIX + id;
        ProductBaseInfo productBaseInfo = redisClient.getObject(key, ProductBaseInfo.class);
        if (Objects.isNull(productBaseInfo)) {
            //第一次查询缓存中没有则进行加锁
            synchronized (ProductMngServiceImpl.class) {
                //再次查redis缓存，如果不存在，则查询数据库，并且将查询到的结果回写进redis
                productBaseInfo = redisClient.getObject(key, ProductBaseInfo.class);
                if (Objects.isNull(productBaseInfo)) {
                    productBaseInfo = productBaseInfoMapper.selectByPrimaryKey(id);
                    if (Objects.isNull(productBaseInfo)) {
                        String msg = String.format("不存在id=%s的商品", id);
                        LOGGER.error(msg);
                        throw new ProductBizException(ResultCodeEnum.QUERY_ERROR, msg);
                    } else {
                        redisClient.add(key, productBaseInfo);
                    }
                }
            }
        }
        return ProductMngServiceUtil.convertProductBaseInfoDO2DTO(productBaseInfo);
    }

    @Override
    public List<ProductBaseInfoDTO> queryAllProduct(int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize);
        ProductBaseInfoExample example = new ProductBaseInfoExample();
        example.setOrderByClause("id desc");
        List<ProductBaseInfo> productBaseInfoDOS = productBaseInfoMapper.selectByExample(example);
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
