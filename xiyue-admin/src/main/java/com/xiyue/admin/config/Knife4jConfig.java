package com.xiyue.admin.config;


import com.xiyue.common.config.BaseKnife4jConfig;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

/***
 * 创建Swagger配置
 * @since:knife4j-springdoc-openapi-demo 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a>
 * 2020/03/15 20:40
 */
@EnableCaching
@Configuration
public class Knife4jConfig extends BaseKnife4jConfig {

    @Override
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .contact(new Contact().name("hcl").email("hclbbq520@gmail.com").url("/"))
                        .title("曦月")
                        .version("1.0")
                        .description("admin文档")
                        .termsOfService("https://com.xiyue")
                        .license(new License().name("Apache 2.0")
                                .url("https://com.xiyue")));
    }
}
