package com.xiyue.admin.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiyue.admin.dto.PostRoleDto;
import com.xiyue.admin.dto.PutRoleDto;
import com.xiyue.admin.dto.PutRoleMenuDto;
import com.xiyue.admin.entity.SysMenu;
import com.xiyue.admin.entity.SysRole;
import com.xiyue.admin.entity.SysRoleMenu;
import com.xiyue.admin.entity.SysUserRole;
import com.xiyue.admin.mapper.SysMenuMapper;
import com.xiyue.admin.mapper.SysRoleMapper;
import com.xiyue.admin.mapper.SysRoleMenuMapper;
import com.xiyue.admin.mapper.SysUserRoleMapper;
import com.xiyue.common.constants.GlobalConstants;
import com.xiyue.common.enums.BusinessErrorCode;
import com.xiyue.common.vo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
public class RoleService extends ServiceImpl<SysRoleMapper, SysRole>{
   @Resource
   private SysUserRoleMapper sysUserRoleMapper;


   @Resource
   private SysRoleMenuMapper sysRoleMenuMapper;



   @Resource
   private CoreService coreService;


   public Result<Page<SysRole>> getRoles(String menuId,Integer pageNum,Integer pageSize){
      return Result.ok(this.page(new Page<>(pageNum, pageSize), new LambdaQueryWrapper<SysRole>().eq(SysRole::getMenuId, menuId)));
   }


   public Result postRole(PostRoleDto postRoleDto){

      SysRole sysRole = BeanUtil.copyProperties(postRoleDto, SysRole.class);


      if (this.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getMenuId, sysRole.getMenuId()).eq(SysRole::getName, sysRole.getName())) > 0) {
         return Result.fail(BusinessErrorCode.ROLE_NAME_DUPLICATION);
      }

      String code = null;

      //随机10位code，数据库里有重复就一直随机，直到不重复
      List<String> codes =  this.list(null).stream()
              .map(SysRole::getCode)
              .collect(Collectors.toList());

      while (true){
         code =  RandomUtil.randomStringUpper(10);
         if (!codes.contains(code)) {
            break;
         }
      }

      sysRole.setCode(code);

      if (this.save(sysRole)) {
         //批量刷新角色菜单缓存
         coreService.batchCachePower();
         return Result.ok();
      }
      return  Result.fail(BusinessErrorCode.SYS_ERROR);
   }

   public Result putRole(PutRoleDto putRoleDto){

      SysRole sysRole = BeanUtil.copyProperties(putRoleDto, SysRole.class);

      SysRole oldSysRole = this.getById(sysRole.getId());

      if (null == oldSysRole) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }

      if (!sysRole.getName().equals(oldSysRole.getName())) {
         if (this.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getMenuId, oldSysRole.getMenuId()).eq(SysRole::getName, sysRole.getName())) > 0) {
            return Result.fail(BusinessErrorCode.ROLE_NAME_DUPLICATION);
         }
      }
      this.updateById(sysRole);

      //批量刷新角色菜单缓存
      coreService.batchCachePower();

      return Result.ok();
   }


   public Result deleteRole(String id){

      //禁止删除超管角色
      if (GlobalConstants.roleId.equals(id)) {
         return Result.fail(BusinessErrorCode.DELETE_DISABLE_ROlE);
      }


      //先把关联的用户 菜单 都移除，再删除，这里就提示，要删除手动把关联用户，菜单都删除，再来调这个接口

      if (this.getById(id) == null) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }

      //查询关联用户
      if (sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id)) > 0) {
         return Result.fail(BusinessErrorCode.REMOVE_THE_ASSOCIATED_USER_FIRST);
      }

      //查询关联菜单
      if (sysRoleMenuMapper.selectCount(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id)) > 0) {
         return Result.fail(BusinessErrorCode.REMOVE_THE_ASSOCIATED_MENU_FIRST);
      }
      if (this.removeById(Long.valueOf(id))) {
         //批量刷新角色菜单缓存
         coreService.batchCachePower();
         return Result.ok();
      }

      return Result.fail(BusinessErrorCode.SYS_ERROR);

   }

   public Result<List<Long>> getOwnMenu(String id){

      if (this.getById(Long.valueOf(id)) == null) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }


     List<Long> menusId = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id)).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

      return Result.ok(menusId);
   }


   public Result putRoleMenu(PutRoleMenuDto putRoleMenuDto){

      if (this.getById(putRoleMenuDto.getRoleId()) == null) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }


      List<Long> menusId = sysRoleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, putRoleMenuDto.getRoleId())).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

      List<Long> missingIds = putRoleMenuDto.getMenuIds().stream()
              .filter(menuId -> !menusId.contains(menuId))
              .collect(Collectors.toList());

      //原来未被选中的菜单就添加
      missingIds.forEach(e -> {
         SysRoleMenu sysRoleMenu = new SysRoleMenu();
         sysRoleMenu.setMenuId(e);
         sysRoleMenu.setRoleId(putRoleMenuDto.getRoleId());
         sysRoleMenuMapper.insert(sysRoleMenu);
      });


      List<Long> ownIds = menusId.stream()
              .filter(menuId -> !putRoleMenuDto.getMenuIds().contains(menuId))
              .collect(Collectors.toList());

      //原来已经选中的菜单没在当前提交数据的就删除
      ownIds.forEach(e -> {
         sysRoleMenuMapper.deleteById(e);
      });

      //批量刷新角色菜单缓存
      coreService.batchCachePower();

      return Result.ok();
   }



























}
