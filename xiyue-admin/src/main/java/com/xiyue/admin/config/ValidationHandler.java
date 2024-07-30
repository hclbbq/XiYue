package com.xiyue.admin.config;


import com.xiyue.common.config.BaseValidationHandler;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class ValidationHandler extends BaseValidationHandler {

}
