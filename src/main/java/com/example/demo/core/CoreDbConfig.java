package com.example.demo.core;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "coreEntityManagerFactory",
        transactionManagerRef = "coreTransactionManager",
        basePackages = {"com.example.demo.core.repo"})
public class CoreDbConfig {

    @Primary
    @Bean(name = "coreDataSourceProperties")
    @ConfigurationProperties("spring.datasource-core")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Primary
    @Bean(name = "coreDataSource")
    @ConfigurationProperties("spring.datasource-primary.configuration")
    public DataSource primaryDataSource(@Qualifier("coreDataSourceProperties") DataSourceProperties primaryDataSourceProperties) {
        return primaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
    @Bean(name = "coreEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }
    @Primary
    @Bean(name = "coreEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            @Qualifier("coreEntityManagerFactoryBuilder") EntityManagerFactoryBuilder coreEntityManagerFactoryBuilder,
            @Qualifier("coreDataSource") DataSource primaryDataSource) {

        return coreEntityManagerFactoryBuilder
                .dataSource(primaryDataSource)
                .packages("com.example.demo.core.domain")
                .persistenceUnit("coreDataSource")
                .build();
    }

    @Primary
    @Bean(name = "coreTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("coreEntityManagerFactory") EntityManagerFactory coreEntityManagerFactory) {

        return new JpaTransactionManager(coreEntityManagerFactory);
    }
}
