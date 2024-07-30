package com.xiyue.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * [Sa-Token 权限认证] 全局配置类
 */
@Slf4j
@Configuration
public class SaTokenConfigure  {

    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico")
                .setAuth(obj -> checkAuth())
                .setBeforeAuth(obj -> setBeforeAuth())
                .setError(this::handleError);
    }


    private void checkAuth() {
        log.info("---------- 进入Sa-Token全局认证 -----------");

        // 所有uri进行登录校验
        SaRouter.match("/**").notMatch(notLogin()).check(StpUtil::checkLogin);

        log.info("admin该用户权限列表：{}", StpUtil.getPermissionList());
        log.info("admin该用户角色列表：{}", StpUtil.getRoleList());

        SaRouter.match("/admin/api/admin/**", r -> StpUtil.checkRole("super-admin"));

        log.info("<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private SaResult handleError(Throwable e) {
        log.info("---------- 进入Sa-Token异常处理 -----------");
        return SaResult.error(e.getMessage());
    }

    private void setBeforeAuth() {
        SaHolder.getResponse()
                // 允许指定域访问跨域资源
                .setHeader("Access-Control-Allow-Origin", "*")
                // 允许所有请求方式
                .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                // 有效时间
                .setHeader("Access-Control-Max-Age", "3600")
                // 允许的header参数
                .setHeader("Access-Control-Allow-Headers", "*");
    }

    public static List<String> notLogin() {
        List<String> allows = new ArrayList<>();
        allows.add("/admin/api/signIn");
        allows.add("/admin/api/signUp");
        allows.add("/admin/v3/**");

        //swagger资源放行
        allows.add("/v2/**");
        allows.add("/v3/**");
        allows.add("/swagger-ui.html/**");
        allows.add("/swagger-ui/**");
        allows.add("/doc.html/**");
        allows.add("/api-docs-ext/**");
        allows.add("/swagger-resources/**");
        allows.add("/webjars/**");
        allows.add("/favicon.ico");
        allows.add("/error");
        return allows;
    }


}

