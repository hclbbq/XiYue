package com.xiyue.admin.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色-权限 关系表
 * </p>
 *
 * @author hcl
 * @since 2023-07-26
 */
@Getter
@Setter
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限/菜单id
     */
    private Long menuId;

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
