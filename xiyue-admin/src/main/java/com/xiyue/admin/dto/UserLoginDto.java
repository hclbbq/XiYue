package com.xiyue.admin.dto;



import com.xiyue.common.enums.DeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
@Schema(description = "用户登录信息")
public class UserLoginDto {

    /**
     * 用户名
     */

    @Schema(description = "账号",defaultValue = "colin",title = "账号-title")
    @NotBlank(message = "account不能为空")
    protected String account;

    /**
     * 密码
     */
    @Schema(description = "密码",defaultValue = "123456",title = "密码-title")
    @NotBlank(message = "password不能为空")
    protected String password;

    /**
     * 登录的设备类型
     */
    @Schema(description = "登录的设备类型",defaultValue = "PC",title = "设备类型")
    protected DeviceType device;


}
