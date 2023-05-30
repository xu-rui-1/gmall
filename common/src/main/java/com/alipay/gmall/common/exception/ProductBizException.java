/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.common.exception;

import com.alipay.gmall.common.enums.ResultCodeEnum;

/**
 * @author ruitu.xr
 * @version ProductBizException.java, v 0.1 2023年05月30日 22:28 ruitu.xr Exp $
 */
public class ProductBizException extends RuntimeException{
    private final String code;

    private final String msg;

    public ProductBizException(ResultCodeEnum resultCodeEnum, String errMsg) {
        super(errMsg);
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getDesc();
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
