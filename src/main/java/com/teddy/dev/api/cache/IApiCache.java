package com.teddy.dev.api.cache;

import java.util.Collection;
import java.util.List;

import com.teddy.dev.api.pojo.ApiVO;

public interface IApiCache<T, K> {

	/**
	 * 获取api列表
	 * 
	 * @return
	 */
	public Collection<T> apis();

	/**
	 * 获取指定的api
	 * 
	 * @param _key
	 * @return null if not found
	 */
	public List<T> get(K _key);

	/**
	 * 向缓存中增加api
	 * 
	 * @param apiVO
	 */
	public void add(ApiVO apiVO);
}
