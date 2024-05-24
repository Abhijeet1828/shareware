package com.custom.sharewise.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * This configuration class is used for customizing the database connection.
 * 
 * @implNote The annotations on the class enable transaction management and JPA
 *           repositories.
 * 
 * @author Abhijeet
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.custom.sharewise.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DatabaseConfiguration {

	private final Environment env;

	public DatabaseConfiguration(Environment env) {
		this.env = env;
	}

	/**
	 * This bean creates a Hikari data source with different connection pooling
	 * properties.
	 * 
	 * @return {@link HikariDataSource}
	 */
	@Primary
	@Bean(name = "dataSource")
	HikariDataSource dataSource() {
		HikariDataSource hikariDataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
		hikariDataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		hikariDataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
		hikariDataSource.setUsername(env.getProperty("spring.datasource.username"));
		hikariDataSource.setPassword(env.getProperty("spring.datasource.password"));
		hikariDataSource.setMaximumPoolSize(env.getProperty("spring.datasource.max-active", Integer.class));
		hikariDataSource.setMinimumIdle(env.getProperty("spring.datasource.max-idle", Integer.class));
		hikariDataSource.setConnectionTestQuery(env.getProperty("spring.datasource.validation-query"));
		hikariDataSource.setConnectionTimeout(env.getProperty("spring.datasource.connection-timeout", Long.class));
		hikariDataSource.setIdleTimeout(env.getProperty("spring.datasource.idle-timeout", Long.class));
		hikariDataSource.setMaxLifetime(env.getProperty("spring.datasource.max-lifetime", Long.class));
		hikariDataSource.setAutoCommit(env.getProperty("spring.datasource.auto-commit", Boolean.class));
		hikariDataSource
				.setLeakDetectionThreshold(env.getProperty("spring.datasource.leak-detection-threshold", Long.class));
		return hikariDataSource;
	}

	/**
	 * This bean creates entity manager factory which scans the entity classes in
	 * the application and defines a JPA vendor.
	 * 
	 * @param dataSource - {@link HikariDataSource}
	 * 
	 * @return {@link LocalContainerEntityManagerFactoryBean}
	 */
	@Primary
	@Bean(name = "entityManagerFactory")
	LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier(value = "dataSource") HikariDataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.custom.sharewise.model");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);

		em.setJpaProperties(additionalProperties());

		return em;
	}

	/**
	 * This bean creates transaction manager which helps add transactions to
	 * queries.
	 * 
	 * @param entityManagerFactory
	 * @return
	 */
	@Primary
	@Bean(name = "transactionManager")
	PlatformTransactionManager transactionManager(
			@Qualifier(value = "entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());

		return jpaTransactionManager;
	}

	/**
	 * This bean creates session factory for hibernate.
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean(name = "sessionFactory")
	LocalSessionFactoryBean sessionFactory(@Qualifier(value = "dataSource") HikariDataSource dataSource) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("com.custom.sharewise.model");
		sessionFactory.setHibernateProperties(additionalProperties());

		return sessionFactory;
	}

	/**
	 * Provides additional hibernate properties.
	 * 
	 * @return
	 */
	private Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		properties.setProperty("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
		properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		return properties;
	}

}
