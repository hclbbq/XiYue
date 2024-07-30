package com.xiyue.admin.config;

import com.xiyue.common.config.BaseJacksonConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class JacksonConfig extends BaseJacksonConfig {

}
