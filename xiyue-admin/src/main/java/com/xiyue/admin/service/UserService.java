package com.xiyue.admin.service;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.xiyue.admin.dto.*;
import com.xiyue.admin.entity.SysRole;
import com.xiyue.admin.entity.SysRoleMenu;
import com.xiyue.admin.entity.SysUser;
import com.xiyue.admin.entity.SysUserRole;
import com.xiyue.admin.mapper.*;
import com.xiyue.common.constants.GlobalConstants;
import com.xiyue.common.enums.BusinessErrorCode;
import com.xiyue.common.enums.UserStatusType;
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
public class UserService extends ServiceImpl<SysUserMapper, SysUser>{


   @Resource
   private SysRoleMapper sysRoleMapper;


   @Resource
   private SysUserRoleMapper sysUserRoleMapper;

   @Resource
   private CoreService coreService;

   public Result<Page<SysUser>> getUsers(Integer pageNum,Integer pageSize){
      return Result.ok(this.page(new Page<>(pageNum, pageSize), null));
   }


   public Result<SysRole> getOwnRole(String menuId){
       return Result.ok(sysRoleMapper.selectOne(
               new MPJLambdaWrapper<SysRole>()
                       .selectAll(SysRole.class)
                       .leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRole::getId)
                       .leftJoin(SysUser.class, SysUser::getId, SysUserRole::getUserId)
                       .eq(SysRole::getMenuId, menuId)
                       .eq(SysUser::getId, StpUtil.getLoginId())));
   }


   public Result putUserRole (PutUserRoleDto putUserRoleDto){
      SysRole sysRole = sysRoleMapper.selectById(putUserRoleDto.getRoleId());

      if (sysRole == null) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }

      if (this.getById(putUserRoleDto.getUserId()) == null) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }


      SysUserRole sysUserRole = sysUserRoleMapper.selectOne(
              new MPJLambdaWrapper<SysUserRole>()
                      .selectAll(SysUserRole.class)
                      .leftJoin(SysUser.class, SysUser::getId, SysUserRole::getUserId)
                      .leftJoin(SysRole.class, SysRole::getId, SysUserRole::getRoleId)
                      .eq(SysRole::getMenuId, sysRole.getMenuId())
                      .eq(SysUser::getId, putUserRoleDto.getUserId()));

      if (null != sysUserRole) {
         if (sysUserRole.getRoleId().equals(putUserRoleDto.getRoleId())) {
            return Result.ok();
         }
      }

      sysUserRoleMapper.deleteById(sysUserRole);

      if (sysUserRoleMapper.insert(BeanUtil.copyProperties(putUserRoleDto, SysUserRole.class)) > 0) {

         //批量刷新角色菜单缓存
         coreService.batchCachePower();

         return Result.ok();
      }

      return Result.fail();
   }



   public Result putUserStatus(PutUserStatusDto putUserStatusDto){

      SysUser sysUser = this.getById(putUserStatusDto.getUserId());

      if (sysUser == null) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }

      sysUser.setStatus(!sysUser.getStatus());

      this.updateById(sysUser);

      if (!sysUser.getStatus()) {
         //封禁账号，那就直接踢人下线,注销token
         StpUtil.kickout(sysUser.getId());
      }

      return Result.ok();
   }


   public Result deleteUser(String id){

      if (GlobalConstants.adminId.equals(id)) {
         return Result.fail(BusinessErrorCode.DELETE_DISABLE_USER);
      }

      if (this.removeById(Long.valueOf(id))) {

         //批量刷新角色菜单缓存
         coreService.batchCachePower();

         return Result.ok();
      }

      return Result.fail();

   }

   /*public Result postRole(PostRoleDto postRoleDto){

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

      return this.save(sysRole) ? Result.ok() : Result.fail(BusinessErrorCode.SYS_ERROR);
   }

   */



























}
