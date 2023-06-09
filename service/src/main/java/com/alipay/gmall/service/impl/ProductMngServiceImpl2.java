/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.service.impl;

import com.alipay.gmall.dto.ProductBaseInfoDTO;
import com.alipay.gmall.service.ProductMngService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ruitu.xr
 * @version ProductMngServiceImpl2.java, v 0.1 2023年06月05日 15:25 ruitu.xr Exp $
 */
@Service("productMngService2")
public class ProductMngServiceImpl2 implements ProductMngService {
    @Override
    public int addProduct(ProductBaseInfoDTO productBaseInfoDTO) {
        return 0;
    }

    @Override
    public int updateProduct(ProductBaseInfoDTO productBaseInfoDTO) {
        return 0;
    }

    @Override
    public void deleteProductById(int id) {

    }

    @Override
    public List<ProductBaseInfoDTO> queryProductInfoByName(String productName) {
        return null;
    }

    @Override
    public ProductBaseInfoDTO queryProductInfoById(Integer id) {
        return null;
    }

    @Override
    public List<ProductBaseInfoDTO> queryAllProduct(int pageIndex, int pageSize) {
        return null;
    }
}
