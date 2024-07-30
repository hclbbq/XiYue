package com.xiyue.admin.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiyue.admin.dto.PostMenuDto;
import com.xiyue.admin.dto.PostRoleDto;
import com.xiyue.admin.dto.PutMenuDto;
import com.xiyue.admin.dto.PutRoleDto;
import com.xiyue.admin.entity.SysMenu;
import com.xiyue.admin.entity.SysRole;
import com.xiyue.admin.entity.SysRoleMenu;
import com.xiyue.admin.entity.SysUserRole;
import com.xiyue.admin.mapper.SysMenuMapper;
import com.xiyue.admin.mapper.SysRoleMapper;
import com.xiyue.admin.mapper.SysRoleMenuMapper;
import com.xiyue.admin.mapper.SysUserRoleMapper;
import com.xiyue.admin.vo.SysMenuTreeVo;
import com.xiyue.common.constants.GlobalConstants;
import com.xiyue.common.enums.BusinessErrorCode;
import com.xiyue.common.enums.MenuType;
import com.xiyue.common.vo.Result;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
public class MenuService extends ServiceImpl<SysMenuMapper, SysMenu>{

   @Resource
   private CoreService coreService;



   public Result<List<SysMenuTreeVo>> getMenuTrees(String menuId){

      SysMenu sysMenu = this.getById(menuId);

      if (null == sysMenu || sysMenu.getType() != MenuType.SYS.getValue()) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }


      List<SysMenu> sysMenus = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getId, menuId));

      // 转换成树形结构
      List<SysMenuTreeVo> sysMenuTreeVos = BeanUtil.copyToList(sysMenus, SysMenuTreeVo.class);

      // 递归构建树
      buildTree(sysMenuTreeVos);

      return Result.ok(sysMenuTreeVos);
   }


   private void buildTree(List<SysMenuTreeVo> nodes) {
      for (SysMenuTreeVo node : nodes) {
         // 查询当前节点的子节点
         List<SysMenu> children = this.list(
                 new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getPid, node.getId()).orderByAsc(SysMenu::getOrd));

         // 如果有子节点，则递归构建子树
         if (!children.isEmpty()) {
            List<SysMenuTreeVo> childTrees = BeanUtil.copyToList(children, SysMenuTreeVo.class);
            node.setChildren(childTrees);
            buildTree(childTrees); // 递归构建子树
         }
      }
   }


   public Result postMenu(PostMenuDto postMenuDto){

        SysMenu sysMenu = BeanUtil.copyProperties(postMenuDto, SysMenu.class);

        //一个系统同一层只能有一个相同名称的菜单
        if (this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getPid, sysMenu.getPid()).eq(SysMenu::getTitle, sysMenu.getTitle())) > 0) {
             return Result.fail(BusinessErrorCode.MENU_NAME_DUPLICATION);
        }

        String code = null;

        //随机10位code，数据库里有重复就一直随机，直到不重复
        List<String> codes =  this.list(null).stream()
              .map(SysMenu::getCode)
              .collect(Collectors.toList());

        while (true){
            code =  RandomUtil.randomStringUpper(10);
            if (!codes.contains(code)) {
                break;
            }
        }

        sysMenu.setCode(code);

        if (this.save(sysMenu)) {
            //批量刷新角色菜单缓存
            coreService.batchCachePower();
            return Result.ok();
        }

      return Result.fail(BusinessErrorCode.SYS_ERROR);
   }

 public Result putMenu(PutMenuDto putMenuDto){

      SysMenu sysMenu = BeanUtil.copyProperties(putMenuDto, SysMenu.class);

      SysMenu oldSysMenu = this.getById(sysMenu.getId());

      if (null == oldSysMenu) {
         return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR);
      }

      if (!sysMenu.getTitle().equals(oldSysMenu.getTitle())) {
         if (this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getPid, oldSysMenu.getPid()).eq(SysMenu::getTitle, sysMenu.getTitle())) > 0) {
            return Result.fail(BusinessErrorCode.MENU_NAME_DUPLICATION);
         }
      }

      this.updateById(sysMenu);

     //批量刷新角色菜单缓存
     coreService.batchCachePower();

      return Result.ok();
   }


   public Result deleteMenu(String id){

       if (Arrays.asList(GlobalConstants.menusId).contains(id)) {
           return Result.fail(BusinessErrorCode.DELETE_DISABLE_MENU);
       }

       if (this.removeById(Long.valueOf(id))) {

           //批量刷新角色菜单缓存
           coreService.batchCachePower();

           return Result.ok();
       }

       return Result.fail(BusinessErrorCode.SYS_ERROR);
   }

}
