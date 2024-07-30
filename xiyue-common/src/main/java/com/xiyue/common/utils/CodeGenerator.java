package com.xiyue.common.utils;


import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


import java.util.Collections;

@Slf4j
public class CodeGenerator {

    //输出目录，改成自己的
    private static String outDir = "E://test";

    private static String dataBasesName = "xi-yue-admin";
    private static String tableName = "sys";

    private static String packageName = "com.xiyue.admin";


    @Getter
    @Setter
    public class User{
        private String name;
    }

    @Getter
    @Setter
    public class Age{
        private Integer age;
    }

    public static void main(String[] args) {

        String json = "[\n" +
                "  {\n" +
                "\t\t\"name\": \"hcl\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"age\": 18\n" +
                "\t}\n" +
                "]";
        //1,字符串转array
        JSONArray jsonArray = JSONUtil.parseArray(json);

        //2,通过下表取到不同的值，然后强转为预设好的对象
        User user = JSONUtil.toBean(jsonArray.getJSONObject(0), User.class);
        Age age = JSONUtil.toBean(jsonArray.getJSONObject(1), Age.class);

        //3，拿到这个对象然后输出
        System.out.println(user.getName() + "---" + age.getAge());



        //   Age age = JSONUtil.toBean(JSONUtil.parseArray(json).get(1).toString(), Age.class);


        //System.out.println(age.getAge());

        // createGenerator();
    }

    public static void createGenerator(){
        FastAutoGenerator.create("jdbc:mysql://192.168.1.76:3306/" +dataBasesName+ "?serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false",
                        "root", "ly666")
                .globalConfig(builder -> {
                    builder.author("hcl") // 设置作者
                            .dateType(DateType.ONLY_DATE)
                            //.enableSwagger() // 开启 swagger 模式
                            .outputDir(outDir); // 指定输出目录

                })
                .packageConfig(builder -> {
                    builder.parent(packageName) // 设置父包名
                            //.moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, outDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableName) // 设置需要生成的表名
                            //.addTablePrefix("t_", "c_") // 设置过滤表前缀
                            .entityBuilder()
                                .enableLombok()
                                .enableFileOverride()
                            .controllerBuilder()
                                .enableRestStyle()
                                .enableFileOverride()
                            .serviceBuilder()
                                .enableFileOverride()
                            .mapperBuilder()
                                .enableBaseColumnList()
                                .enableBaseResultMap()
                                .enableFileOverride()



                    ;
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }




}
