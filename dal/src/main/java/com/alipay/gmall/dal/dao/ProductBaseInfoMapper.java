package com.alipay.gmall.dal.dao;

import com.alipay.gmall.dal.domain.ProductBaseInfo;
import com.alipay.gmall.dal.domain.ProductBaseInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ProductBaseInfoMapper {
    long countByExample(ProductBaseInfoExample example);

    int deleteByExample(ProductBaseInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductBaseInfo record);

    int insertSelective(ProductBaseInfo record);

    int insertSelectiveWithGeneratedKey(ProductBaseInfo record);

    List<ProductBaseInfo> selectByExample(ProductBaseInfoExample example);

    ProductBaseInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProductBaseInfo record, @Param("example") ProductBaseInfoExample example);

    int updateByExample(@Param("record") ProductBaseInfo record, @Param("example") ProductBaseInfoExample example);

    int updateByPrimaryKeySelective(ProductBaseInfo record);

    int updateByPrimaryKey(ProductBaseInfo record);
}