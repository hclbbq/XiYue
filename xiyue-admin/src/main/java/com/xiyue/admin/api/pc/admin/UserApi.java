package com.xiyue.admin.api.pc.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.xiyue.admin.dto.*;
import com.xiyue.admin.entity.SysRole;
import com.xiyue.admin.entity.SysUser;
import com.xiyue.admin.service.RoleService;
import com.xiyue.admin.service.UserService;
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

import java.util.List;

;


@Tag(name = "用户控制器")
@ApiSort(3)
@RestController
@RequestMapping("/api/admin/user")
public class UserApi {

    @Resource
    private UserService userService;


    @Operation(summary = "获取用户列表",description = "获取菜单")
    @GetMapping("/users/{pageNum}/{pageSize}")
    @Parameters({
            @Parameter(name = "pageNum",description = "当前页",in = ParameterIn.PATH, required = true,schema = @Schema(type = "integer")),
            @Parameter(name = "pageSize",description = "页长度",in = ParameterIn.PATH, required = true,schema = @Schema(type = "integer"))
    })
    public Result<Page<SysUser>> getUsers(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        return userService.getUsers(pageNum,pageSize);
    }


    @Operation(summary = "获取该用户对应的角色(去调角色接口里的角色列表，再来调用这个返回的角色id，判断是否渲染该用户有这个角色)",description = "获取该用户对应的角色")
    @GetMapping("/ownRole/{menuId}")
    @Parameters({
            @Parameter(name = "menuId",description = "系统id",in = ParameterIn.PATH, required = true,schema = @Schema(type = "string"))
    })
    public Result<SysRole> getOwnRole(@PathVariable String menuId) {
        return userService.getOwnRole(menuId);
    }


    @Operation(summary = "编辑用户对应的角色",description = "编辑用户对应的角色")
    @PutMapping("/userRole")
    public Result putUserRole(@RequestBody @Valid PutUserRoleDto putUserRoleDto) {
        return userService.putUserRole(putUserRoleDto);
    }

    @Operation(summary = "用户状态切换",description = "用户状态切换")
    @PutMapping("/status")
    public Result putUserStatus(@RequestBody @Valid PutUserStatusDto putUserStatusDto) {
        return userService.putUserStatus(putUserStatusDto);
    }

    @Operation(summary = "删除用户",description = "删除用户")
    @DeleteMapping("/{id}")
    @Parameters({
            @Parameter(name = "id",description = "用户id",in = ParameterIn.PATH, required = true,schema = @Schema(type = "string"))
    })
    public Result deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }


    /* @Operation(summary = "在系统下创建角色",description = "创建角色")
    @PostMapping("")
    public Result postRole(@RequestBody @Valid PostRoleDto postRoleDto) {
        return roleService.postRole(postRoleDto);
    }*/
}
