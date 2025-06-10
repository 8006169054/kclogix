package com.kclogix.common.config.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import kainos.framework.core.KainosKey;

@Configuration // 설정파일 어노테이션
@EnableTransactionManagement // 트랜잭션 범위를 활성화 해 주는 어노테이션
@EnableJpaRepositories( // 특정 repository 패키지를 찾아 어떤 트랜잭션에 어떤 repo를 연결할 지 설정 해 주는 어노테이션
    entityManagerFactoryRef = KainosKey.DBConfig.DataBase.PrimaryEntityManagerFactory,
    transactionManagerRef = KainosKey.DBConfig.DataBase.PrimaryTransactionManager
    )
public class PrimaryDataBase {

	@Bean(name = KainosKey.DBConfig.DataBase.PrimaryDataSource, destroyMethod = "close")
	@Primary
	@ConfigurationProperties(prefix = "kainos.datasource.primary")
    DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

	@Bean(name = KainosKey.DBConfig.DataBase.PrimaryEntityManagerFactory)
	@Primary
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
    		@Qualifier(KainosKey.DBConfig.DataBase.PrimaryDataSource) DataSource dataSource,
    		EntityManagerFactoryBuilder builder) {
        return builder
          .dataSource(dataSource)
          .packages("com.kclogix")
          .persistenceUnit(KainosKey.DBConfig.DataBase.PrimaryUnitName)
          .build();
    }

    @Bean(name = KainosKey.DBConfig.DataBase.PrimaryTransactionManager)
    @Primary
    PlatformTransactionManager transactionManager(
             @Qualifier(KainosKey.DBConfig.DataBase.PrimaryEntityManagerFactory) EntityManagerFactory entityManagerFactory)  {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
