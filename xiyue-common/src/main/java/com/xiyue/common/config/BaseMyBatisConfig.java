package com.xiyue.common.config;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.xiyue.common.enums.DeleteFlag;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * MyBatis相关配置
 *
 */
@Configuration
@MapperScan({"com.xiyue.**.mapper"})
public class BaseMyBatisConfig implements MetaObjectHandler {



    @Autowired
    private BaseApiConfig apiContext;
    /**
     * 分页插件,并配置多租户，通过tenantId,暂时关闭,需要用的时候，把注释打开就行
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // TenantLineInnerInterceptor
      /*  interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                Long currentProviderId = apiContext.getCurrentSysId();
                if (null == currentProviderId) {
                    throw new RuntimeException("Get CurrentProviderId error.");
                }
                // 实际可以将TenantId放在threadLocale中(比如xxxxContext中)，并获取。
                return new LongValue(currentProviderId);
            }

            @Override
            public String getTenantIdColumn() {
                return "sys_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {

                // 是否需要需要过滤某一张表
                List<String> tableNameList = Arrays.asList("sys_user","sys_role_menu","sys_user_role","sys_menu","sys_role);

                if (tableNameList.contains(tableName)){
                    return true;
                }else{
                    return false;
                }

            }

            @Override
            public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
                return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
            }

        }));*/


        //分页插件 （ps：如果项目中有用到分页插件可以添加如下这行代码，但是必须要写在多租户插件后面 也就是先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor）
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        pageInterceptor.setMaxLimit(500L);
        // 开启 count 的 join 优化,只针对部分 left join
        pageInterceptor.setOptimizeJoin(true);
        interceptor.addInnerInterceptor(pageInterceptor);

        //阻止恶意或误操作的全表更新删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }


    @Override
    public void insertFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "createTime", new Date());
        this.fillStrategy(metaObject, "updateTime", new Date());
        this.fillStrategy(metaObject, "deleteFlag", DeleteFlag.NOT_DELETE.getValue());

        Object userId = StpUtil.getLoginId();
        if(null != userId){
            this.strictInsertFill(metaObject, "createUser", String.class, userId.toString());
            this.strictUpdateFill(metaObject, "updateUser", String.class, userId.toString());
        }

   }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.fillStrategy(metaObject, "updateTime", new Date());
        Object userId = StpUtil.getLoginId();
        if(null != userId){
            this.strictUpdateFill(metaObject, "updateUser", String.class, userId.toString());
        }


    }




}
