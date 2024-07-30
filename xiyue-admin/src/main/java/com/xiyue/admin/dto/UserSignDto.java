package com.xiyue.admin.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class UserSignDto {

    /**
     * 用户姓名
     */
    @Schema(description = "用户姓名",title = "姓名")
    private String name;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称",title = "昵称")
    @NotBlank(message = "用户昵称不能为空")
    private String nickName;

    /**
     * 账号
     */

    @Schema(description = "账号",title = "账号")
    @NotBlank(message = "账号不能为空")
    protected String account;

    /**
     * 手机号
     */

    @Schema(description = "手机号",title = "手机号")
    protected String phone;

    /**
     * 密码
     */
    @Schema(description = "密码",title = "密码")
    @NotBlank(message = "password不能为空")
    protected String password;

}
