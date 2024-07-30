package com.xiyue.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 定义业务错误码
 */

@AllArgsConstructor
@Getter
public enum BusinessErrorCode {

    SYS_ERROR(500,"系统异常"),

    FORBIDDEN_TO_CALL(9000, "禁止调用"),

    ARGS_VALID_ERROR(10001, "参数校验失败"),

    LOGIN_USER_ILLEGAL(10002, "非法登录用户"),

    LOGIN_EXPIRED(10003, "登录已失效"),

    NO_TOKEN_ERROR(10004, "未传入token"),

    NOT_PERMISSION_ERROR(10005, "权限不足"),

    NO_HAVE_LOGIN_USER(10006, "请登录"),

    USER_PHONE_ERROR(10007, "账号或密码错误"),

    TWO_TIME_PWD_ERROR(10008, "两次密码输入不一致"),

    PHONE_EXIST_ERROR(10009, "当前手机号已存在"),

    NO_ROLE(10010, "没有角色"),

    EXISTING_ACCOUNT(10011, "你可以使用现有账户登录，也可以创建一个新账户"),

    ROLE_NAME_DUPLICATION(10012, "角色名称重复"),

    REMOVE_THE_ASSOCIATED_USER_FIRST(10013, "请先移除关联的用户"),

    REMOVE_THE_ASSOCIATED_MENU_FIRST(10013, "请先移除关联的菜单"),

    MENU_NAME_DUPLICATION(10012, "菜单名称重复"),

    ACCOUNT_BLOCKING(10013, "账号异常，联系管理员解封"),

    DELETE_DISABLE_USER (10014, "超级管理员用户禁止删除"),

    DELETE_DISABLE_ROlE (10015, "超级管理员角色禁止删除"),

    DELETE_DISABLE_MENU (10016, "超级管理员菜单禁止删除"),

    ;

    private int code;
    private String errMessage;

    public BusinessErrorCode formatErrMessage(Object... params) {
        this.setErrMessage(String.format(this.getErrMessage(), params));
        return this;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}