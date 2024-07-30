package com.xiyue.admin.api.pc.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.xiyue.admin.dto.PostMenuDto;
import com.xiyue.admin.dto.PutMenuDto;
import com.xiyue.admin.dto.PutRoleDto;
import com.xiyue.admin.service.MenuService;
import com.xiyue.admin.service.RoleService;
import com.xiyue.admin.vo.SysMenuTreeVo;
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


@Tag(name = "菜单控制器")
@ApiSort(5)
@RestController
@RequestMapping("/api/admin/menu")
public class MenuApi {

    @Resource
    private MenuService menuService;


    @Operation(summary = "获取该系统下的树状菜单列表",description = "获取菜单")
    @GetMapping("/menuTrees/{menuId}")
    @Parameters({
            @Parameter(name = "id",description = "菜单id",in = ParameterIn.PATH, required = true,schema = @Schema(type = "string"))
    })
    public Result<List<SysMenuTreeVo>> getMenuTrees(@PathVariable String menuId) {
        return menuService.getMenuTrees(menuId);
    }


    @Operation(summary = "在系统下创建菜单",description = "创建菜单")
    @PostMapping("")
    public Result postMenu(@RequestBody @Valid PostMenuDto postMenuDto) {
        return menuService.postMenu(postMenuDto);
    }

    @Operation(summary = "在系统下修改菜单",description = "修改菜单")
    @PutMapping("")
    public Result putMenu(@RequestBody @Valid PutMenuDto putMenuDto) {
        return menuService.putMenu(putMenuDto);
    }


    @Operation(summary = "在系统下删除菜单",description = "删除菜单")
    @DeleteMapping("/{id}")
    @Parameters({
            @Parameter(name = "id",description = "菜单id",in = ParameterIn.PATH, required = true,schema = @Schema(type = "string"))
    })
    public Result deleteMenu(@PathVariable String id) {
        return menuService.deleteMenu(id);
    }
}
