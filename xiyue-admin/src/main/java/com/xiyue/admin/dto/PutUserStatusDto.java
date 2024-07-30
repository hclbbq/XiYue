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
public class PutUserStatusDto {



    @Schema(description = "用户id",defaultValue = "",title = "用户id-id")
    @NotNull(message = "用户id不能为空")
    protected Long userId;


}
