package com.xiyue.admin.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author hcl
 * @since 2023-07-26
 */
@Getter
@Setter
@TableName("sys_role")
@ToString
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 菜单里的系统id,保证了每个角色归属不同系
     */
    private Long menuId;

    /**
     * 名称
     */
    private String name;

    /**
     * Key值
     */
    private String code;

    /**
     * 角色描述
     */
    private String remark;

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
