package com.ming.m_blog.mybaitsPlusCodeAuth;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 *  mybatisPlus自动生成代码
 */
public class CodeAutogenerate {

    public static void main(String[] args) {
        // 创建一个代码生成器对象
        AutoGenerator mpg = new AutoGenerator();

        // 配置策略
        GlobalConfig globalConfig = new GlobalConfig();
        // 获取当前系统目录
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/src/main/java");  // 想要输出到项目的这个目录下
        globalConfig.setAuthor("Ming");  // 作者注释
        globalConfig.setOpen(false);   // 是否打开资源管理器
        globalConfig.setFileOverride(false);  // 是否覆盖
        // globalConfig.setServiceName("%sService"); // 去掉Service的前缀

        //-----------------------------------------
        // globalConfig.setIdType(IdType.ASSIGN_ID);   // 生成主键的Id类型
        //-----------------------------------------

        globalConfig.setDateType(DateType.ONLY_DATE);  // 设置日期类型
        globalConfig.setSwagger2(true);  // 开启Swagger
        mpg.setGlobalConfig(globalConfig);  // 将配置的策略添加到mpg代码生成器对象中

        // 设置数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/blog?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai"); // 数据库地址
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");  // 设置驱动
        dataSourceConfig.setUsername("root");   // 数据库账号
        dataSourceConfig.setPassword("123456");   // 数据库密码
        dataSourceConfig.setDbType(DbType.MYSQL);  // DbType数据库类型
        mpg.setDataSource(dataSourceConfig);   // 将配置的数据源设置到mpg代码生成器对象中

        // 包的配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName("m_blog");  // 设置模块名字
        packageConfig.setParent("com.ming");  // 设置代码生成的位置
        packageConfig.setEntity("pojo");   // 设置实体类的名字
        packageConfig.setMapper("mapper");  // 设置dao层包名
        packageConfig.setService("service");  // 设置service包名
        packageConfig.setController("controller");  // 设置controller层包名
        mpg.setPackageInfo(packageConfig);  // 将包的配置信息传递给mpg代码生成器对象中

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("unique_view");   // 设置要映射的表名,
        strategy.setNaming(NamingStrategy.underline_to_camel);  // 设置包的命名规则,这里是下划线转驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);  // 数据库字段策略
        strategy.setEntityLombokModel(true);   // 自动生成Lombok
        strategy.setLogicDeleteFieldName("is_delete");  // 设置逻辑删除，参数为逻辑删除的字段名
        // 自动填充策略
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);  // 创建时填充
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);  // 修改时填充
        List<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategy.setTableFillList(tableFills);  // 将自动填充策略添加到策略配置中
        // 乐观锁配置
        strategy.setVersionFieldName("version");  // 设置配置乐观锁的字段

        strategy.setRestControllerStyle(true);    // 开启Restful风格的驼峰命名格式
        strategy.setControllerMappingHyphenStyle(true);  // localhost:8080/hello_id_2
        mpg.setStrategy(strategy);  // 将配置策略设置到mpg代码生成器中

        mpg.execute();  // 执行自动代码生成器
    }

}
