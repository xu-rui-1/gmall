package com.alipay.gmall.service;

import com.alipay.gmall.dto.ProductBaseInfoDTO;

import java.util.List;

/**
 * @author ruitu.xr
 * @version ProductMngService.java, v 0.1 2023年05月30日 13:40 ruitu.xr Exp $
 */
public interface ProductMngService {
    /**
     * 添加产品
     * @param productBaseInfoDTO
     * @return
     */
    Integer addProduct(ProductBaseInfoDTO productBaseInfoDTO);

    /**
     * 更新产品信息
     * @param productBaseInfoDTO
     * @return
     */
    Integer updateProduct(ProductBaseInfoDTO productBaseInfoDTO);

    /**
     * 根据名称删除产品
     * @param name
     */
    void deleteProductByName(String name);

    /**
     * 根据名称查询产品
     * @param name
     * @return
     */
    ProductBaseInfoDTO queryProductInfoByName(String name);

    /**
     * 根据id查询产品
     * @param id
     * @return
     */
    ProductBaseInfoDTO queryProductInfoById(Integer id);

    /**
     * 查询所有产品
     * @return
     */
    List<ProductBaseInfoDTO> queryAllProduct();
}
