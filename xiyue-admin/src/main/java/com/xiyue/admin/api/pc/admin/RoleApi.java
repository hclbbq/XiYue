package com.xiyue.admin.api.pc.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.xiyue.admin.dto.PostRoleDto;
import com.xiyue.admin.dto.PutRoleDto;
import com.xiyue.admin.dto.PutRoleMenuDto;
import com.xiyue.admin.dto.UserLoginDto;
import com.xiyue.admin.entity.SysMenu;
import com.xiyue.admin.entity.SysRole;
import com.xiyue.admin.service.RoleService;
import com.xiyue.common.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

;import java.util.List;


@Tag(name = "角色控制器")
@ApiSort(4)
@RestController
@RequestMapping("/api/admin/role")
public class RoleApi {

    @Resource
    private RoleService roleService;


    @Operation(summary = "获取该系统下的角色列表",description = "获取菜单")
    @GetMapping("/roles/{menuId}/{pageNum}/{pageSize}")
    @Parameters({
            @Parameter(name = "id",description = "菜单id",in = ParameterIn.PATH, required = true,schema = @Schema(type = "string")),
            @Parameter(name = "pageNum",description = "当前页",in = ParameterIn.PATH, required = true,schema = @Schema(type = "integer")),
            @Parameter(name = "pageSize",description = "页长度",in = ParameterIn.PATH, required = true,schema = @Schema(type = "integer"))
    })
    public Result<Page<SysRole>> getRoles(@PathVariable String menuId, @PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        return roleService.getRoles(menuId,pageNum,pageSize);
    }


    @Operation(summary = "在系统下创建角色",description = "创建角色")
    @PostMapping("")
    public Result postRole(@RequestBody @Valid PostRoleDto postRoleDto) {
        return roleService.postRole(postRoleDto);
    }

    @Operation(summary = "在系统下修改角色",description = "修改角色")
    @PutMapping("")
    public Result putRole(@RequestBody @Valid PutRoleDto putRoleDto) {
        return roleService.putRole(putRoleDto);
    }


    @Operation(summary = "在系统下删除角色",description = "删除角色")
    @DeleteMapping("/{id}")
    @Parameters({
            @Parameter(name = "id",description = "角色id",in = ParameterIn.PATH, required = true,schema = @Schema(type = "string"))
    })
    public Result deleteRole(@PathVariable String id) {
        return roleService.deleteRole(id);
    }



    @Operation(summary = "获取该角色对应的菜单列表(去调菜单接口里的树状列表，再来调用这个返回的list id列表，判断是否渲染该角色有这个菜单)",description = "获取角色拥有的菜单列表")
    @GetMapping("/ownMenu/{id}")
    @Parameters({
            @Parameter(name = "id",description = "角色id",in = ParameterIn.PATH, required = true,schema = @Schema(type = "string")),
    })
    public Result<List<Long>> getOwnMenu(@PathVariable String id) {
        return roleService.getOwnMenu(id);
    }


    @Operation(summary = "编辑角色对应的菜单",description = "编辑角色对应的菜单")
    @PutMapping("/roleMenu")
    public Result putRoleMenu(@RequestBody @Valid PutRoleMenuDto putRoleMenuDto) {
        return roleService.putRoleMenu(putRoleMenuDto);
    }

}
