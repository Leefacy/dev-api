package com.teddy.dev;

import com.teddy.dev.api.ApiConfiguration;
import com.teddy.dev.api.cache.IApiCache;

public class AppConfiguration<T, K> {

	private ApiConfiguration apiConfiguration;

	private IApiCache<T, K> apiCache;

	public ApiConfiguration getApiConfiguration() {
		return apiConfiguration;
	}

	public void setApiConfiguration(ApiConfiguration apiConfiguration) {
		this.apiConfiguration = apiConfiguration;
	}

	public IApiCache<T, K> getApiCache() {
		return apiCache;
	}

	public void setApiCache(IApiCache<T, K> apiCache) {
		this.apiCache = apiCache;
	}

}
