package com.sereneast.orchestramdm.keysight.mdmcustom.config;

import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({ApplicationProperties.class})
public class ApplicationConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean(name="oracleDbDataSource")
    public DataSource primaryDataSource()  {
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setDriverClassName(environment.getProperty("keysight.datasource.oracle.driver-class-name"));
        ds.setUrl(environment.getProperty("keysight.datasource.oracle.url"));
        ds.setUsername(environment.getProperty("keysight.datasource.oracle.username"));
        ds.setPassword(environment.getProperty("keysight.datasource.oracle.password"));
        ds.setInitialSize(2);
        ds.setMaxActive(10);
        ds.setMaxIdle(5);
        ds.setMinIdle(2);
        ds.setMaxWait(2000);
        return ds;
    }

    @Bean(name="hsqlDbDataSource")
    @Primary
    public DataSource batchDataSource(){
        return DataSourceBuilder.create()
                .url("jdbc:hsqldb:mem:jobrepo")
                .username("ashish")
                .password("ashish")
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate oracleDbNamedParameterJdbcTemplate(@Qualifier("oracleDbDataSource")DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
