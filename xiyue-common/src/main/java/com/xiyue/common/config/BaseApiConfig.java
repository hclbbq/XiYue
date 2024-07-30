package com.xiyue.common.config;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @description 系统的上下文帮助类。ConcurrentHashMap设置租户ID，供后续的MP的getSysId()取出
 */
@Component
public class BaseApiConfig {

        private static final String KEY_CURRENT_SYS_ID = "KEY_CURRENT_SYS_ID";
        private static final Map<String, Object> mContext = new ConcurrentHashMap<>();

        public void setCurrentSysId(Long providerId) {
            mContext.put(KEY_CURRENT_SYS_ID, providerId);
        }

        public Long getCurrentSysId() {
            return (Long) mContext.get(KEY_CURRENT_SYS_ID);
        }

}