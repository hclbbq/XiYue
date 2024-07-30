package com.xiyue.admin.api;

import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.xiyue.admin.vo.SysMenuListVo;
import com.xiyue.admin.service.PowerService;
import com.xiyue.admin.vo.SysMenuTreeVo;
import com.xiyue.common.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "获取左侧导航栏和首页系统列表")
@ApiSort(2)
@RestController
@RequestMapping("/api/power")
public class PowerApi {

    @Resource
    private PowerService powerService;

    @Operation(summary = "获取全部系统",description = "获取全部系统,根据字段判断有无权限进入这个系统")
    @GetMapping("/allSys")
    public Result<List<SysMenuListVo>> getAllSys() {
        return powerService.getAllSys();
    }



    @Operation(summary = "获取菜单",description = "获取菜单")
    @GetMapping("/ownMenus/{menuId}")
    public Result<SysMenuTreeVo> getOwnMenus(@Parameter(name = "menuId",required = true, in = ParameterIn.PATH,description = "菜单id",schema = @Schema(type = "string")) @PathVariable String menuId) {
        return powerService.getOwnMenus(menuId);
    }


}
