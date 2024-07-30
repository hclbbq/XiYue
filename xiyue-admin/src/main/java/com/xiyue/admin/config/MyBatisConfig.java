package com.xiyue.admin.config;

import com.xiyue.common.config.BaseMyBatisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis相关配置
 *
 */
@EnableCaching
@Configuration
public class MyBatisConfig extends BaseMyBatisConfig {


}
