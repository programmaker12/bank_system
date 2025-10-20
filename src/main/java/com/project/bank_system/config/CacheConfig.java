package com.project.bank_system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class CacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(new ConcurrentMapCache("accounts") {
            @Override
            public ValueWrapper get(Object key) {
                ValueWrapper value = super.get(key);
                if (value == null) {
                    logger.info("Cache MISS for key: {}", key);
                } else {
                    logger.info("Cache HIT for key: {}", key);
                }
                return value;
            }
        }));
        manager.initializeCaches();
        return manager;
    }


    public void printCacheContents(CacheManager cacheManager) {
        Cache accountsCache = cacheManager.getCache("accounts");
        if (accountsCache instanceof ConcurrentMapCache concurrentMapCache) {
            Map<Object, Object> nativeCache = concurrentMapCache.getNativeCache();
            nativeCache.forEach((key, value) -> {
                // valueWrapper is a SimpleValueWrapper
                //Object value = ((Cache.ValueWrapper) valueWrapper).get();
                logger.info("Cache Key: {}, Value: {}", key, value);

            });
        }
    }

}
