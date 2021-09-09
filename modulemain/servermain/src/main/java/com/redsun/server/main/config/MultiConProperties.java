package com.redsun.server.main.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;

@Configuration

@ConfigurationProperties(prefix = "multitenancy.servermain")
@EncryptablePropertySource(name = "EncryptedProperties", value = "classpath:application.yml")
public class MultiConProperties {

	private List<DataSourceProperties> dataSourcesProps;

	public List<DataSourceProperties> getDataSources() {
		return this.dataSourcesProps;
	}

	public void setDataSources(List<DataSourceProperties> dataSourcesProps) {
		this.dataSourcesProps = dataSourcesProps;
	}

	public static class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

		private String conId;

		public String getConId() {
			return conId;
		}

		public void setConId(String conId) {
			this.conId = conId;
		}
	}

}
