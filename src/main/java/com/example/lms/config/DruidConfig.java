package com.example.lms.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description druid属性配置
 */
@Configuration
public class DruidConfig {
    /**
     * 配置指定
     * 如果maven已经引入jpa则不需要加上初始化以及销毁方法，如果使用的是mybatis并且没有引入jpa，
     * 则需要加上(destroyMethod = "close", initMethod = "init")
     * @return datasource
     */
    @Bean(destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druid() {
        return new DruidDataSource();
    }

    /**
     * 配置一个监控的filter
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> webStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());

        Map<String,String> initParams = new HashMap<>();
        //忽略资源
        initParams.put("exclusions","*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");

        bean.setInitParameters(initParams);
        bean.setUrlPatterns(Arrays.asList("/*"));

        return bean;
    }

    /**
     * 配置监控过滤器
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean =  new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        //账号
        initParams.put("loginUsername","admin");
        // 密码
        initParams.put("loginPassword","123456");
        // 默认允许所有
        initParams.put("allow","");
        //不允许的黑名单ip
        initParams.put("deny","192.168.123.22");

        bean.setInitParameters(initParams);
        return bean;
    }

}
