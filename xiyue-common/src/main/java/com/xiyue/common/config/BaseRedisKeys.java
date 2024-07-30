package com.xiyue.common.config;

public class BaseRedisKeys {

    private static final String rootPath = "xiYue:";

    private static final String menus = "menus:";

    private static final String roles = "roles:";

    public static final String clientMenus =  rootPath + "client:" + menus;

    public static final String clientRoles =  rootPath + "client:" + roles;

    public static final String serverMenus =  rootPath + "server:" + menus;

    public static final String serverRoles =  rootPath + "server:" + roles;

    public static final String UNDERSCORE  = "_";
}
