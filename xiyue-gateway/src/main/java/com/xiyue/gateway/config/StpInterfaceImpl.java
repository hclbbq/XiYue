package com.xiyue.gateway.config;

import cn.dev33.satoken.stp.StpInterface;

import jakarta.annotation.Resource;
import com.xiyue.common.config.BaseRedisKeys;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private RedisConfig redisConfig;


    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //用户有很多角色，得从redis里取出list角色id，然后便利把菜单code列出来
        List<String> menus = new ArrayList<>();
        if(redisConfig.redisService().hasKey(BaseRedisKeys.clientRoles + loginId)){
            List<String> roles =  Arrays.asList(redisConfig.redisService().get(BaseRedisKeys.clientRoles + loginId).toString().split(BaseRedisKeys.UNDERSCORE));

            roles.forEach(a -> {

                if(redisConfig.redisService().hasKey(BaseRedisKeys.serverMenus + a)){

                    Arrays.asList(redisConfig.redisService().get(BaseRedisKeys.serverMenus + a).toString()).forEach(b -> {
                        menus.add(b);
                    });
                }

            });
        }

        return menus;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roles = new ArrayList<>();
        if(redisConfig.redisService().hasKey(BaseRedisKeys.serverRoles + loginId)){
            roles = Arrays.asList(redisConfig.redisService().get(BaseRedisKeys.serverRoles + loginId).toString().split(BaseRedisKeys.UNDERSCORE));
        }

        return roles;
    }
}
