/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.service.impl;

import com.alipay.gmall.dal.dao.ProductBaseInfoDAO;
import com.alipay.gmall.dal.domain.ProductBaseInfoDO;
import com.alipay.gmall.dto.ProductBaseInfoDTO;
import com.alipay.gmall.service.ProductMngService;
import com.alipay.gmall.utils.ProductMngServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public Integer addProduct(ProductBaseInfoDTO productBaseInfoDTO) {
        ProductBaseInfoDO productBaseInfoDO = ProductMngServiceUtil.convertProductBaseInfoDTO2DO(productBaseInfoDTO);
        int ret = productBaseInfoDAO.insertSelective(productBaseInfoDO);
        if (ret <= 0) {

            return -1;
        }
        return productBaseInfoDO.getId();
    }

    @Override
    public Integer updateProduct(ProductBaseInfoDTO productBaseInfoDTO) {
        int ret = -1;
        Integer id = productBaseInfoDTO.getId();
        if (id == null) {
            LOGGER.error("产品id为空，请传入产品id");
            return ret;
        }
        ProductBaseInfoDO productBaseInfoDO = ProductMngServiceUtil.convertProductBaseInfoDTO2DO(productBaseInfoDTO);
        ret = productBaseInfoDAO.updateByPrimaryKeySelective(productBaseInfoDO);
        if (ret <= 0) {
            LOGGER.error("由于网络抖动原因，更新产品信息失败，请联系技术同学....");
            return ret;
        }

        return productBaseInfoDO.getId();
    }

    @Override
    public void deleteProductByName(String name) {

    }

    @Override
    public ProductBaseInfoDTO queryProductInfoByName(String name) {
        return null;
    }

    @Override
    public ProductBaseInfoDTO queryProductInfoById(Integer id) {
        return null;
    }

    @Override
    public List<ProductBaseInfoDTO> queryAllProduct() {
        return null;
    }
}
