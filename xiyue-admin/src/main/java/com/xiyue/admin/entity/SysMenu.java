package com.xiyue.admin.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.xiyue.common.enums.MenuType;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统菜单/权限表
 * </p>
 *
 * @author hcl
 * @since 2023-07-26
 */
@Getter
@Setter
@TableName("sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;


    /**
     * 父级ID
     */
    private Long pid;

    /**
     * 文本内容
     */
    private String title;

    /**
     * 链接的url
     */
    private String url;

    /**
     * 菜单的icon
     */
    private String icon;

    /**
     * 权限标识符：对于后台控制类定义。示例：user:list
     */
    private String code;

    /**
     * 权限类型：0- 系统| 1 - 主目录 | 2 - 菜单| 3 - 按钮 |4-网页链接
     */
    private Integer type;

    /**
     * 菜单 排序
     */
    private Integer ord;

    /**
     *状态：  0-隐藏 | 1-显示
     */
    private Boolean status;

    /**
     * 删除标志
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private String deleteFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建用户
     */
    @TableField(fill = FieldFill.INSERT,select = false)
    private String createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,select = false)
    private String updateUser;
}
