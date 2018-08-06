package com.karen.moneylizer.datasource;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceConfiguration {
	@Bean
	@ConfigurationProperties("app.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
}
