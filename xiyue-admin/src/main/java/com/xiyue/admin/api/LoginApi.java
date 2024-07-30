package com.xiyue.admin.api;



import cn.dev33.satoken.annotation.SaIgnore;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.xiyue.common.enums.BusinessErrorCode;
import com.xiyue.common.utils.ValidationUtil;
import com.xiyue.common.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



import com.xiyue.admin.dto.UserLoginDto;
import com.xiyue.admin.dto.UserSignDto;
import com.xiyue.admin.service.LoginService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "登录注册")
@ApiSort(1)
@RestController
@RequestMapping("/api")
public class LoginApi {

    @Autowired
    private LoginService loginService;


    @SaIgnore
    @Operation(summary = "用户登录",description = "用户登录详情")
    @PostMapping("/signIn")
    public Result<Object> signIn(@RequestBody @Valid UserLoginDto userLoginDto) {
        return loginService.signIn(userLoginDto);
    }


    @SaIgnore
    @Operation(summary = "用户注册",description = "用户注册")
    @PostMapping("/signUp")
    public Result<Object> signUp(@Valid @RequestBody UserSignDto userSignDto) {
        return loginService.signUp(userSignDto);
    }

}
