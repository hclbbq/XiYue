package com.xiyue.admin.service;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiyue.admin.config.RedisConfig;
import com.xiyue.admin.entity.SysMenu;
import com.xiyue.admin.entity.SysRole;
import com.xiyue.admin.mapper.SysMenuMapper;
import com.xiyue.admin.mapper.SysRoleMapper;
import com.xiyue.admin.vo.SysMenuListVo;
import com.xiyue.admin.vo.SysMenuTreeVo;
import com.xiyue.common.config.BaseRedisKeys;
import com.xiyue.common.enums.BusinessErrorCode;
import com.xiyue.common.enums.MenuType;
import com.xiyue.common.vo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hcl
 * @since 2022-04-25
 */
@Slf4j
@Service
public class PowerService extends ServiceImpl<SysMenuMapper, SysMenu>{

   @Resource
   private RedisConfig redisConfig;


   @Resource
   private SysRoleMapper sysRoleMapper;



   @Resource
   private CoreService coreService;

   public Result<List<SysMenuListVo>> getAllSys() {

      List<SysMenu> sysMenus = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getType, MenuType.SYS.getValue()));

      List<SysMenuListVo> sysMenuTreeDtoList = BeanUtil.copyToList(sysMenus, SysMenuListVo.class);

      String loginId = (String) StpUtil.getLoginId();

      sysMenuTreeDtoList.forEach(a -> {
         AtomicBoolean own = new AtomicBoolean(false);
         coreService.getMenus(loginId).forEach(b -> {
            if (Objects.equals(a.getId(), JSONUtil.toBean(b, SysMenuTreeVo.class).getId())) {
               own.set(true);
            }
         });
         a.setOwn(own.get());
      });

      return Result.ok(sysMenuTreeDtoList);
   }
   public Result<SysMenuTreeVo> getOwnMenus(String menuId) {

      String loginId = (String) StpUtil.getLoginId();

      if(redisConfig.redisService().hasKey(BaseRedisKeys.clientRoles + loginId)){
         List<String> roles =  Arrays.asList(redisConfig.redisService().get(BaseRedisKeys.clientRoles + loginId).toString().split(BaseRedisKeys.UNDERSCORE));

         //判断角色列表里是否拥有这个菜单, 本系统目前一个用户只能有一个角色，没有做一用户对多角色
         Long roleId = sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().in(SysRole::getId, roles)).stream()
                 .filter(sysRole -> Objects.equals(String.valueOf(sysRole.getMenuId()), menuId))
                 .map(SysRole::getId)
                 .findFirst()
                 .orElse(-1L);;


         if (roleId == -1L) {
            return Result.fail(BusinessErrorCode.NOT_PERMISSION_ERROR);
         }

         if(redisConfig.redisService().hasKey(BaseRedisKeys.clientMenus + roleId)){
            return Result.ok(JSONUtil.toBean(redisConfig.redisService().get(BaseRedisKeys.clientMenus + roleId).toString(), SysMenuTreeVo.class));
         }

      }

      return Result.fail(BusinessErrorCode.SYS_ERROR);

   }

   public Result<Page<SysRole>> getRoles(String menuId,Integer pageNum,Integer pageSize){
      return Result.ok(sysRoleMapper.selectPage(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<SysRole>().eq(SysRole::getMenuId, menuId)));
   }



























}
