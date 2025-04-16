package prateek_gupta.SampleProject.multitenancy;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TenantRegistration implements WebMvcConfigurer {
    @Autowired
    private TenantInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Intercepting all paths for Tenant
        registry.addInterceptor(tenantInterceptor).addPathPatterns("/**");
    }

    /**
     * Creating a bean of {@link LocalContainerEntityManagerFactoryBean} which will
     * resolve the schema name to connect to by using {@link TenantResolver} and
     * connect to the schema by using the class {@link TenantConnectionProvider}
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            MultiTenantConnectionProvider multiTenantConnectionProvider,
            CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl) {
        Map<String, Object> properties = new HashMap<>();
        // Setting the strategy for MultiTenancy as by using schemas
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);

        // Setting TenantConnectionProvider (implementation) to be used for
        // connecting to schema
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                multiTenantConnectionProvider);

        // Setting TenantResolver (implementation) to be used for resolving schema
        // to connect to
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
                currentTenantIdentifierResolverImpl);

        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();
        // Setting DataSource/Driver to connect to Database (No need to create been
        // its handled already, even its autowired in TenantConnectionProvider)
        entityManagerFactory.setDataSource(dataSource);

        // Package to search for the implementations for MultiTenantConnectionProvider
        // and CurrentTenantIdentifierResolver
        entityManagerFactory.setPackagesToScan("prateek_gupta.SampleProject");

        // Setting Adapter to for JPA
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Setting properties for JPA
        entityManagerFactory.setJpaPropertyMap(properties);
        return entityManagerFactory;
    }
}
