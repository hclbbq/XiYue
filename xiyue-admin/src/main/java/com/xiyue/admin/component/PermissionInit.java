package com.xiyue.admin.component;

import com.xiyue.admin.service.CoreService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * 初始化把权限数据存入redis，让网关使用
 */
@Slf4j
@Component
public class PermissionInit implements ApplicationRunner {



    @Resource
    private CoreService coreService;



    //前端做了用户的角色列表和角色的菜单列表渲染数据，前端调用接口，就能知道对应的角色id然后根据角色id返回菜单列表给前端渲染
    //后端做了用户的角色列表和角色的菜单列表code鉴权，前端调用接口，如果这个接口访问用户没有这个角色权限或者菜单权限就验证
    @Override
    public void run(ApplicationArguments args) throws Exception {;

        log.info("初始化权限数据据填充---------------------------------");

        coreService.batchCachePower();

        log.info("初始化权限数据填充结束---------------------------------");
    }

}

