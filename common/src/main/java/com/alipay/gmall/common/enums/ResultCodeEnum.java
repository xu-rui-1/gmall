/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.common.enums;

/**
 * @author ruitu.xr
 * @version ResultCodeEnum.java, v 0.1 2023年05月30日 22:23 ruitu.xr Exp $
 */
public enum ResultCodeEnum {
    SUCCESS("SUCCESS", "成功"),
    INVALID_PARAMETER("INVALID_PARAMETER", "无效参数"),
    ILLEGAL_PARAMETER("ILLEGAL_PARAMETER", "非法参数"),
    QUERY_ERROR("PRODUCT_QUERY_ERROR","查询失败"),
    INSERT_ERROR("PRODUCT_INSERT_ERROR", "插入失败"),
    UPDATE_ERROR("PRODUCT_UPDATE_ERROR", "更新失败"),
    DELETE_ERROR("PRODUCT_DELETE_ERROR", "删除失败"),
    ;

    private String code;

    private String desc;

    ResultCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
