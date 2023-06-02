/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.service.impl;

import com.alipay.gmall.spi.service.IHelloService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author ruitu.xr
 * @version IHelloServiceImpl.java, v 0.1 2023年06月01日 21:45 ruitu.xr Exp $
 */
@Service
@DubboService
public class IHelloServiceImpl implements IHelloService {
    @Override
    public String hello() {
        return "test";
    }
}
