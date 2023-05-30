/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.gmall.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ruitu.xr
 * @version EnvConfig.java, v 0.1 2023年05月30日 12:32 ruitu.xr Exp $
 */
@Configuration
public class EnvConfig {
    @Value("${spring.title}")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
