package com.xiyue.gateway.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import com.xiyue.common.enums.BusinessErrorCode;
import com.xiyue.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 未登录
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NotLoginException.class)
    public Result<BusinessErrorCode> notLoginExceptionHandler(NotLoginException e) {
        errorFixedPosition(e);
        log.error("_> 错误原因：");
        log.error("_> {}", e.getMessage());
        log.error("=============================错误打印完毕=============================");
        return Result.fail(BusinessErrorCode.NO_HAVE_LOGIN_USER);
    }

    /**
     * 角色异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {NotRoleException.class, NotPermissionException.class})
    public Result<BusinessErrorCode> notRoleExceptionHandler(Exception e) {
        errorFixedPosition(e);
        log.error("_> 错误原因：");
        log.error("_> {}", e.getMessage());
        log.error("=============================错误打印完毕=============================");
        return Result.fail(BusinessErrorCode.NOT_PERMISSION_ERROR);
    }


    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<List<String>> constraintViolationHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        LinkedList<String> errors = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(violations)) {
            violations.forEach(constraintViolation -> errors.add(constraintViolation.getMessage()));
        }

        errorFixedPosition(e);
        log.error("_> 错误原因：");
        log.error("_> {}", e.getMessage());
        log.error("=============================错误打印完毕=============================");
        return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR, errors);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<List<String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        LinkedList<String> errors = Lists.newLinkedList();
        if (!CollectionUtils.isEmpty(allErrors)) {
            allErrors.forEach(objectError -> errors.add(objectError.getDefaultMessage()));
        }

        errorFixedPosition(e);
        log.error("_> 错误原因：");
        log.error("_> {}", e.getMessage());
        log.error("=============================错误打印完毕=============================");
        return Result.fail(BusinessErrorCode.ARGS_VALID_ERROR, errors);
    }

    /**
     * 处理空指针的异常
     *
     * @param e 参数
     * @return 返回异常信息
     */
    @ExceptionHandler(value = NullPointerException.class)
    public Result<BusinessErrorCode> exceptionHandler(NullPointerException e) {
        errorFixedPosition(e);
        log.error("_> 错误原因：");
        log.error("_> {}", e.getMessage());
        log.error("=============================错误打印完毕=============================");
        return Result.fail(BusinessErrorCode.SYS_ERROR);
    }

    /**
     * 处理其他异常
     *
     * @param e 参数
     * @return 返回异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result<BusinessErrorCode> exceptionHandler(Exception e) {
        errorFixedPosition(e);
        log.error("_> 错误原因：");
        log.error("_> {}", e.getMessage());
        log.error("=============================错误打印完毕=============================");
        return Result.fail(BusinessErrorCode.SYS_ERROR);
    }

    /**
     * 定位错误发生的位置
     *
     * @param e 错误参数
     */
    private void errorFixedPosition(Exception e) {
        final StackTraceElement stackTrace = e.getStackTrace()[0];
        final String className = stackTrace.getClassName();
        final int lineNumber = stackTrace.getLineNumber();
        final String methodName = stackTrace.getMethodName();
        e.printStackTrace();
        log.error("=============================错误信息如下=============================");
        log.error("_> 异常定位：");
        log.error("_> 类[{}] ==> 方法[{}] ==> 所在行[{}]\n", className, methodName, lineNumber);
    }
}

