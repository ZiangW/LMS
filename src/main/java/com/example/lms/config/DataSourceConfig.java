package com.example.lms.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Optional;

/**
 * @author 李辉辉
 * @date 3/30/21 2:08 PM
 * @description 数据库配置
 */
//@Slf4j
//@Configuration
//@EnableTransactionManagement
public class DataSourceConfig {
	private static final String DATASOURCE_NAME = "dataSource";

	private static final String MAPPER_PACKAGE = "com.example.lms.dao";


	@Bean(name = "sessionFactory4business")
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Qualifier(DATASOURCE_NAME) DataSource dataSource) throws Exception {
		return createSqlSessionFactoryFromDataSource(dataSource, "classpath:mappers/*.xml");
	}

	@Bean(name = "businessMapperScanner")
	@Primary
	public MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setBasePackage(MAPPER_PACKAGE);
		configurer.setSqlSessionFactoryBeanName("sessionFactory4business");
		return configurer;
	}

	@Bean(name = "businessTransactionManager")
	public DataSourceTransactionManager masterTransactionManager(@Qualifier(DATASOURCE_NAME) DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}


	private SqlSessionFactory createSqlSessionFactoryFromDataSource(DataSource dataSource, String mapperResource) throws Exception {
		Resource[] resources = null;
		resources = new PathMatchingResourcePatternResolver().getResources(mapperResource);

		return createSqlSessionFactoryFromDataSource(dataSource, resources, ExecutorType.SIMPLE);
	}

	private  SqlSessionFactory createSqlSessionFactoryFromDataSource(DataSource dataSource, Resource[] resources, ExecutorType extype) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);

		Optional.ofNullable(resources).ifPresent(sqlSessionFactoryBean::setMapperLocations);

		// 通用mapper
		tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
		configuration.setMapperHelper(new MapperHelper());
		sqlSessionFactoryBean.setConfiguration(configuration);

		SqlSessionFactory sqlSessionFactory;

		sqlSessionFactory = sqlSessionFactoryBean.getObject();
		sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
		sqlSessionFactory.getConfiguration().setJdbcTypeForNull(JdbcType.NULL);
		sqlSessionFactory.getConfiguration().setDefaultExecutorType(extype);

		return sqlSessionFactory;
	}
}
