package com.example.data_service.Config;

import com.example.data_service.Constant.DBTypeEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource master(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slave(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource myDatasource(@Qualifier("master") DataSource master, @Qualifier("slave") DataSource slave){
        Map<Object, Object> mysource = new HashMap<>();
        mysource.put(DBTypeEnum.Master, master);
        mysource.put(DBTypeEnum.Slave, slave);
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(master);
        routingDataSource.setTargetDataSources(mysource);
        return routingDataSource;
    }


}
