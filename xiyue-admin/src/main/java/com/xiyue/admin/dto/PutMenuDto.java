package com.xiyue.admin.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * hcl
 * 用于用户登录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "编辑菜单")
public class PutMenuDto {

    @Schema(description = "id",defaultValue = "",title = "菜单id-pid")
    @NotNull(message = "菜单id不能为空")
    protected Long id;


    @Schema(description = "父级ID",defaultValue = "",title = "父级ID-pid")
    @NotNull(message = "父级ID不能为空")
    protected Long pid;


    @Schema(description = "菜单名称",defaultValue = "",title = "菜单名称-title")
    @NotBlank(message = "菜单名称不能为空")
    protected String title;

    @Schema(description = "链接url",defaultValue = "",title = "链接url-url")
    protected String url;


    @Schema(description = "菜单图标",defaultValue = "",title = "菜单图标-icon")
    protected String icon;


    @Schema(description = "权限类型：0- 系统| 1 - 主目录 | 2 - 菜单| 3 - 按钮 |4-网页链接",defaultValue = "",title = "权限类型-type")
    @NotNull(message = "权限类型不能为空")
    protected Integer type;

    @Schema(description = "菜单排序",defaultValue = "",title = "菜单排序-ord")
    @NotNull(message = "菜单排序不能为空")
    protected Integer ord;

    @Schema(description = "状态：  0-隐藏 | 1-显示",defaultValue = "",title = "状态-status")
    protected Boolean status;


}
