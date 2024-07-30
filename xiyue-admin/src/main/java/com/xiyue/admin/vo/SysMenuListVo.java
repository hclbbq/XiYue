package com.xiyue.admin.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * hcl
 * 树状菜单结构
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "列表菜单结构")
public class SysMenuListVo {

    /**
     * 用户名
     */
    @Schema(description = "菜单id",title = "菜单id")
    protected Long id;

    /**
     * 父级ID
     */
    @Schema(description = "父级ID",title = "父级ID")
    private Long pid;

    /**
     * 文本内容
     */
    @Schema(description = "文本内容",title = "文本内容")
    private String title;

    /**
     * 链接的url
     */
    @Schema(description = "链接的url",title = "链接的url")
    private String url;

    /**
     * 菜单的icon
     */
    @Schema(description = "菜单的icon",title = "菜单的icon")
    private String icon;

    /**
     * 权限标识符：对于后台控制类定义。示例：user:list
     */
    @Schema(description = "权限标识符：对于后台控制类定义。示例：user:list",title = "权限标识符")
    private String code;

    /**
     * 权限类型：0- 系统| 1 - 主目录 | 2 - 菜单| 3 - 按钮 |4-网页链接
     */
    @Schema(description = "权限类型：0- 系统| 1 - 主目录 | 2 - 菜单| 3 - 按钮 |4-网页链接",title = "权限类型")
    private Integer type;

    /**
     * 菜单 排序
     */
    @Schema(description = "菜单 排序",title = "排序")
    private Integer ord;

    /**
     *状态：  0-隐藏 | 1-显示
     */
    @Schema(description = "状态：  0-隐藏 | 1-显示 ",title = "状态：")
    private Boolean status;

    /**
     *状态：  true-拥有 | false-未拥有
     */
    @Schema(description = "true-拥有 | false-未拥有",title = "拥有状态")
    private Boolean own;
}
