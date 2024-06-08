package vn.com.hugio.common.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import vn.com.hugio.common.config.property.DatabaseProperty;
import vn.com.hugio.common.log.LOG;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "vn.com.**.repository",
        entityManagerFactoryRef = "postgresqlEntityManager",
        transactionManagerRef = "postgresqlTransactionManager"
)
//@ConditionalOnProperty(
//value = "postgresql",
//havingValue = "true"
//)
public class DatabaseConfig {

    private final DatabaseProperty databaseProperty;

    @Autowired
    public DatabaseConfig(DatabaseProperty databaseProperty) {
        this.databaseProperty = databaseProperty;
    }

    @Bean("postgresqlDataSources")
    public DataSource mysqlDataSources() {
        LOG.info("CREATE BEAN mysqlDataSources, Connecting to DB URL %s", this.databaseProperty.getUrl());
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.databaseProperty.getDriverClassName());
        dataSource.setUrl(this.databaseProperty.getUrl());
        dataSource.setUsername(this.databaseProperty.getUsername());
        dataSource.setPassword(this.databaseProperty.getPassword());

        return dataSource;
    }

    @Bean("postgresqlEntityManager")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManager(@Qualifier("postgresqlDataSources") DataSource mysqlDataSources) {
        LOG.info("CREATE BEAN mysqlEntityManager");
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mysqlDataSources);

        // Scan Entities in Package:
        em.setPackagesToScan("vn.com.**.entity");
        em.setPersistenceUnitName("HUGIO_DB"); // Important !!

        //
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();

        // JPA & Hibernate
        properties.put(AvailableSettings.DIALECT, this.databaseProperty.getDialect());
        properties.put(AvailableSettings.SHOW_SQL, this.databaseProperty.getShowSql());
        properties.put(AvailableSettings.FORMAT_SQL, true);
        properties.put(AvailableSettings.HBM2DDL_AUTO, this.databaseProperty.getDdlAuto());
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

        em.setJpaPropertyMap(properties);
        em.afterPropertiesSet();
        return em;
    }

    @Bean("postgresqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(@Qualifier("postgresqlEntityManager") LocalContainerEntityManagerFactoryBean mysqlEntityManager) {
        LOG.info("CREATE BEAN mysqlTransactionManager");
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mysqlEntityManager.getObject());
        return transactionManager;
    }

}
