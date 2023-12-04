package io.github.jdouglas9025.doggolifeapi.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "io.github.jdouglas9025.doggolifeapi.repository.relational",
        transactionManagerRef = "jpaTransactionManager"
)
@EnableTransactionManagement
public class MySQLConfig {
    @Bean(name = "mysql")
    @ConfigurationProperties(prefix = "spring.mysql")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Bean(name = "jpaTransactionManager")
    public JpaTransactionManager mysqlTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
