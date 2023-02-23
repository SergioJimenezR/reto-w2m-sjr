package com.w2m.sergiojimenez.retow2msjr.cache;

import java.util.logging.Logger;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

// Fuente de referencia: https://www.baeldung.com/spring-boot-ehcache

public class CacheEventLogger implements CacheEventListener<Object, Object> {

	private static final Logger LOGGER = Logger.getLogger(CacheEventLogger.class.getName());

	@Override
	public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
		LOGGER.info("Cache: Key: " + cacheEvent.getKey() + ". Valor antiguo: " + cacheEvent.getOldValue()
				+ ". Valor nuevo: " + cacheEvent.getNewValue() + ".");
	}
}
