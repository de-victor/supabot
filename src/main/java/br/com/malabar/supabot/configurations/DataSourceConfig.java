package br.com.malabar.supabot.configurations;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource getDataSource() {
        
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url(dbPath());
        dataSourceBuilder.username("sa");
        
        return dataSourceBuilder.build();
    }

    private String dbPath(){
        File file = new File("speedDB");
        String absolutePath = file.getAbsolutePath();
        file.delete();
        
        return "jdbc:h2:file:"+absolutePath;
    }
}
