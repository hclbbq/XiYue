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
public class PutRoleDto {



    @Schema(description = "角色id",defaultValue = "",title = "角色id-id")
    @NotNull(message = "角色id不能为空")
    protected Long id;


    @Schema(description = "角色名字",defaultValue = "",title = "角色名字-name")
    @NotBlank(message = "角色名字不能为空")
    protected String name;

    @Schema(description = "备注",defaultValue = "",title = "角色备注-remark")
    protected String remark;


}
