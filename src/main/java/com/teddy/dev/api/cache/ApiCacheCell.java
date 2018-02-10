package com.teddy.dev.api.cache;

import com.teddy.dev.api.pojo.ApiVO;

public class ApiCacheCell {

	private ApiVO apiVO; // api实体
	private String key; // key

	/**
	 * @param apiVO
	 * @param key
	 */
	public ApiCacheCell(ApiVO apiVO, String key) {
		super();
		this.apiVO = apiVO;
		this.key = key;
	}

	public ApiVO getApiVO() {
		return apiVO;
	}

	public void setApiVO(ApiVO apiVO) {
		this.apiVO = apiVO;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
