package com.sereneast.orchestramdm.keysight.mdmcustom.config;

import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.DatabaseProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.EbxProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({DatabaseProperties.class, EbxProperties.class, RestProperties.class})
@EnableCaching
public class ApplicationConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<ConcurrentMapCache> caches = new ArrayList<ConcurrentMapCache>();
        caches.add(new ConcurrentMapCache("mainCache"));
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean(name="ebxDbDataSource")
    public DataSource primaryDataSource()  {
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
        ds.setDriverClassName(environment.getProperty("keysight.database.ebx.driverClassName"));
        ds.setUrl(environment.getProperty("keysight.database.ebx.url"));
        ds.setUsername(environment.getProperty("keysight.database.ebx.username"));
        ds.setPassword(environment.getProperty("keysight.database.ebx.password"));
        ds.setInitialSize(2);
        ds.setMaxActive(10);
        ds.setMaxIdle(5);
        ds.setMinIdle(2);
        ds.setMaxWait(2000);
        return ds;
    }

    @Bean
    public NamedParameterJdbcTemplate ebxDbNamedParameterJdbcTemplate(@Qualifier("ebxDbDataSource")DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
