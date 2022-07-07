package com.example.demo.integ;

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
        entityManagerFactoryRef = "integEntityManagerFactory",
        transactionManagerRef = "integDbConfigTransactionManager",
        basePackages = {"com.example.demo.integ.repo"})
public class IntegDbConfig {

    @Primary
    @Bean(name = "integDataSourceProperties")
    @ConfigurationProperties("spring.datasource-integ")
    public DataSourceProperties integDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Primary
    @Bean(name = "integDataSource")
    @ConfigurationProperties("spring.datasource-integ.configuration")
    public DataSource integDataSource(@Qualifier("integDataSourceProperties") DataSourceProperties primaryDataSourceProperties) {
        return primaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
    @Bean(name = "integEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }
    @Primary
    @Bean(name = "integEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean integEntityManagerFactory(
            @Qualifier("integEntityManagerFactoryBuilder") EntityManagerFactoryBuilder coreEntityManagerFactoryBuilder,
            @Qualifier("integDataSource") DataSource primaryDataSource) {

        return coreEntityManagerFactoryBuilder
                .dataSource(primaryDataSource)
                .packages("com.example.demo.integ.domain")
                .persistenceUnit("integDataSource")
                .build();
    }

    @Primary
    @Bean(name = "integDbConfigTransactionManager")
    public PlatformTransactionManager integTransactionManager(
            @Qualifier("integEntityManagerFactory") EntityManagerFactory coreEntityManagerFactory) {

        return new JpaTransactionManager(coreEntityManagerFactory);
    }
}
