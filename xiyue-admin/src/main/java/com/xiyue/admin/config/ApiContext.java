package com.xiyue.admin.config;

import com.xiyue.common.config.BaseApiConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 系统的上下文帮助类。ConcurrentHashMap设置租户ID，供后续的MP的getSysId()取出
 */
@Component
@EnableCaching
public class ApiContext extends BaseApiConfig {


}
