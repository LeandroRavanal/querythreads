package io.github.lr.querythreads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.lr.querythreads.entities.Baby;
import io.github.lr.querythreads.services.DBSearchService;

/**
 * Beans de servicios de consulta a Base de Datos. 
 * 
 * @author lravanal
 *
 */
@Configuration
public class ConfigurationServices {

	@Bean
	public DBSearchService<Baby> babyDBService() {
		return new DBSearchService<Baby>() {};
	}
	
}
