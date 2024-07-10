package com.custom.sharewise.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to create cache configuration.
 * 
 * @author Abhijeet
 *
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

	/**
	 * Creating a bean using {@link ConcurrentMapCacheManager} which is a basic
	 * cache for testing purposes. It can be replaced with better tools.
	 * 
	 * @return
	 */
	@Bean
	CacheManager cacheManager() {
		ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("users");
		cacheManager.setAllowNullValues(false);

		return cacheManager;
	}

}
