package com.xiyue.admin.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * hcl
 * 用于用户登录
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "新增角色")
public class PutRoleMenuDto {

    @Schema(description = "角色id",defaultValue = "",title = "角色id-roleId")
    @NotEmpty
    protected Long roleId;


    @Schema(description = "菜单id列表",defaultValue = "",title = "菜单id列表-menuIds")
    @NotEmpty
    protected List<Long> menuIds;
}
