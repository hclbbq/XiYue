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
@Schema(description = "新增角色")
public class PostRoleDto {



    @Schema(description = "菜单id",defaultValue = "",title = "菜单id-menuId")
    @NotNull(message = "菜单id不能为空")
    protected Long menuId;


    @Schema(description = "角色名字",defaultValue = "",title = "角色名字-name")
    @NotBlank(message = "角色名字不能为空")
    protected String name;

    @Schema(description = "备注",defaultValue = "",title = "角色备注-remark")
    protected String remark;


}
