package com.xiyue.admin.service;



import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.xiyue.admin.config.RedisConfig;
import com.xiyue.admin.entity.*;
import com.xiyue.admin.mapper.SysMenuMapper;
import com.xiyue.admin.mapper.SysRoleMapper;
import com.xiyue.admin.mapper.SysUserMapper;
import com.xiyue.admin.vo.SysMenuTreeVo;
import com.xiyue.common.config.BaseRedisKeys;
import com.xiyue.common.enums.MenuType;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  权限核心逻辑模块
 * </p>
 *
 * @author hcl
 * @since 2024=07-29
 */
@Slf4j
@Service
public class CoreService {

   @Resource
   private SysUserMapper sysUserMapper;

   @Resource
   private SysMenuMapper sysMenuMapper;

   @Resource
   private SysRoleMapper sysRoleMapper;

   @Resource
   private RedisConfig redisConfig;


   public void batchCachePower(){

      batchCacheRole();

      batchCacheMenu();

      batchCacheSaTokenRole();

      batchCacheSaTokenMenu();

   }


   /**
    * 批量缓存角色，key为用户id,value为角色id
    */
   private void batchCacheRole(){
      sysUserMapper.selectList(null).forEach(e -> {

         List<SysRole> sysRoles =  sysRoleMapper.selectList(
                 new MPJLambdaWrapper<SysRole>()
                         .selectAll(SysRole.class)
                         .leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRole::getId)
                         .leftJoin(SysUser.class, SysUser::getId, SysUserRole::getUserId)
                         .eq(SysUser::getId, e.getId()));

         if (!sysRoles.isEmpty()) {
            redisConfig.redisService().set(BaseRedisKeys.clientRoles + e.getId(), sysRoles.stream()
                    .map(SysRole::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(BaseRedisKeys.UNDERSCORE)));
         }

      });
   }

   /**
    * 批量缓存菜单，key为角色id,value为菜单树状层级列表
    */
   private void batchCacheMenu(){
      //菜单塞入redis,角色id为key,value为树状菜单，前端用来渲染
      List<SysMenu> frontEndMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getType, MenuType.SYS.getValue()));

      frontEndMenus.forEach(a -> {

         sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().eq(SysRole::getMenuId, a.getId())).forEach(b -> {

            List<SysMenu> sysMenus =  sysMenuMapper.selectList(
                    new MPJLambdaWrapper<SysMenu>()
                            .selectAll(SysMenu.class)
                            .leftJoin(SysRoleMenu.class, SysRoleMenu::getMenuId, SysMenu::getId)
                            .leftJoin(SysRole.class, SysRole::getId, SysRoleMenu::getRoleId)
                            .eq(SysRole::getId, b.getId())
                            .orderByAsc(SysMenu::getOrd));
            // 转换成树形结构
            List<SysMenuTreeVo> sysMenuTreeDtoList = BeanUtil.copyToList(sysMenus, SysMenuTreeVo.class);
            sysMenuTreeDtoList = buildTree(sysMenuTreeDtoList);
            if (!sysMenuTreeDtoList.isEmpty()) {
               redisConfig.redisService().set(BaseRedisKeys.clientMenus + b.getId(), JSONUtil.parseObj(sysMenuTreeDtoList.get(0)).toString());
            }


         });

      });
   }



   /**
    * 批量缓存satoken权限框架角色，key为用户id,value为角色code,用来后端做角色鉴权
    */
   private void batchCacheSaTokenRole(){

      sysUserMapper.selectList(null).forEach(e -> {

         List<SysRole> sysRoles =  sysRoleMapper.selectList(
                 new MPJLambdaWrapper<SysRole>()
                         .selectAll(SysRole.class)
                         .leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRole::getId)
                         .leftJoin(SysUser.class, SysUser::getId, SysUserRole::getUserId)
                         .eq(SysUser::getId, e.getId()));

         if (!sysRoles.isEmpty()) {
            redisConfig.redisService().set(BaseRedisKeys.serverRoles + e.getId(), sysRoles.stream()
                    .map(SysRole::getCode)
                    .map(String::valueOf)
                    .collect(Collectors.joining(BaseRedisKeys.UNDERSCORE)));
         }

      });

   }

   /**
    * 缓存satoken权限框架菜单，key为角色id,value为菜单code,用来后端做菜单鉴权
    */
   private void batchCacheSaTokenMenu(){

      //权限code塞入redis,角色id为key,value为权限code，后端用来鉴权
      List<SysMenu> backEndMenus = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getType, MenuType.SYS.getValue()));

      backEndMenus.forEach(a -> {

         sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().eq(SysRole::getMenuId, a.getId())).forEach(b -> {

            List<SysMenu> sysMenus =  sysMenuMapper.selectList(
                    new MPJLambdaWrapper<SysMenu>()
                            .selectAll(SysMenu.class)
                            .leftJoin(SysRoleMenu.class, SysRoleMenu::getMenuId, SysMenu::getId)
                            .leftJoin(SysRole.class, SysRole::getId, SysRoleMenu::getRoleId)
                            .eq(SysRole::getId, b.getId()));

            // 构建层级 code 列表
            List<String> hierarchicalCodes = buildHierarchicalCodes(sysMenus);

            if (!hierarchicalCodes.isEmpty()) {
               redisConfig.redisService().set(BaseRedisKeys.serverMenus + b.getId(), JSONUtil.parseArray(hierarchicalCodes).toString());
            }

         });

      });

   }

   /**
    *  获取当前用户角色id列表
    * @return
    */
   public List<String> getRoles(String loginId){

      List<String> roles = new ArrayList<>();
      if(redisConfig.redisService().hasKey(BaseRedisKeys.clientRoles + loginId)){
         roles = Arrays.asList(redisConfig.redisService().get(BaseRedisKeys.clientRoles + loginId).toString().split(BaseRedisKeys.UNDERSCORE));
      }

      return roles;
   }

   /**
    *  获取当前用户树状菜单列表
    * @param loginId
    * @return
    */
   public List<String> getMenus(String loginId){

      //用户有很多角色，得从redis里取出list角色id，然后便利把菜单code列出来
      List<String> menus = new ArrayList<>();
      if(redisConfig.redisService().hasKey(BaseRedisKeys.clientRoles + loginId)){
         List<String> roles =  Arrays.asList(redisConfig.redisService().get(BaseRedisKeys.clientRoles + loginId).toString().split(BaseRedisKeys.UNDERSCORE));

         roles.forEach(a -> {

            if(redisConfig.redisService().hasKey(BaseRedisKeys.clientMenus + a)){

               Arrays.asList(redisConfig.redisService().get(BaseRedisKeys.clientMenus + a).toString()).forEach(b -> {
                  menus.add(b);
               });
            }

         });
      }

      return menus;
   }

   /**
    *  获取当前用户satoken格式角色列表
    * @return
    */
   public List<String> getSaTokenRole(String loginId){

      List<String> roles = new ArrayList<>();
      if(redisConfig.redisService().hasKey(BaseRedisKeys.serverRoles + loginId)){
         roles = Arrays.asList(redisConfig.redisService().get(BaseRedisKeys.serverRoles + loginId).toString().split(BaseRedisKeys.UNDERSCORE));
      }

      return roles;
   }



   /**
    *  获取当前用户satoken格式菜单列表
    * @return
    */
   public List<String> getSaTokenMenu(String loginId){

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



   /**
    * 将平铺的列表转换为树状结构
    * @param menuList 平铺的菜单列表
    * @return 树状菜单列表
    */
   private static List<SysMenuTreeVo> buildTree(List<SysMenuTreeVo> menuList) {
      List<SysMenuTreeVo> tree = new ArrayList<>();

      // 找出根节点
      List<SysMenuTreeVo> rootNodes = menuList.stream()
              .filter(menu -> menu.getPid() == null)
              .sorted((i1, i2) -> Integer.compare(i1.getOrd(), i2.getOrd()))
              .collect(Collectors.toList());
      // 将每个根节点添加到树中，并递归设置子节点
      for (SysMenuTreeVo rootNode : rootNodes) {
         buildChildren(rootNode, menuList);
         tree.add(rootNode);
      }

      return tree;
   }

   /**
    * 递归设置子节点
    * @param parent 父节点
    * @param menuList 平铺的菜单列表
    */
   private static void buildChildren(SysMenuTreeVo parent, List<SysMenuTreeVo> menuList) {
      List<SysMenuTreeVo> children = menuList.stream()
              .filter(menu -> parent.getId().equals(menu.getPid()))
              .sorted((i1, i2) -> Integer.compare(i1.getOrd(), i2.getOrd()))
              .collect(Collectors.toList());

      if (!children.isEmpty()) {
         parent.setChildren(children);
         for (SysMenuTreeVo child : children) {
            buildChildren(child, menuList);
         }
      } else {
         parent.setChildren(new ArrayList<>());
      }
   }


   /**
    * 构建层级 code 列表
    * @param menuList 平铺的菜单列表
    * @return 层级 code 列表
    */
   private static List<String> buildHierarchicalCodes(List<SysMenu> menuList) {
      List<String> result = new ArrayList<>();
      // 过滤掉 pid 为 null 的元素并单独处理
      Map<Long, List<SysMenu>> menuMap = menuList.stream()
              .filter(menu -> menu.getPid() != null)
              .collect(Collectors.groupingBy(SysMenu::getPid));

      // 找到根节点并开始构建层级关系
      menuList.stream()
              .filter(menu -> menu.getPid() == null) // 处理 pid 为 null 或 0 的根节点
              .forEach(root -> {
                 result.add(root.getCode());
                 System.out.println(root.getCode() + "BBCC");
                 buildCodeHierarchy(root, menuMap, root.getCode(), result);
              });

      return result;
   }

   /**
    * 递归构建层级 code
    * @param menu 当前菜单
    * @param menuMap 菜单映射
    * @param parentCode 父级 code
    * @param result 结果列表
    */
   private static void buildCodeHierarchy(SysMenu menu, Map<Long, List<SysMenu>> menuMap, String parentCode, List<String> result) {
      List<SysMenu> children = menuMap.get(menu.getId());
      if (children != null) {
         for (SysMenu child : children) {
            String code = parentCode + "." + child.getCode();
            result.add(code);
            buildCodeHierarchy(child, menuMap, code, result);
         }
      }
   }


}
