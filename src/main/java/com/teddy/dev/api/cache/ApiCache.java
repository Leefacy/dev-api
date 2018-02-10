package com.teddy.dev.api.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.teddy.dev.api.pojo.ApiVO;
import com.teddy.dev.util.Utils;

public class ApiCache implements IApiCache<ApiCacheCell, String> {

	private Map<String, List<ApiCacheCell>> apiCache;
	private List<ApiCacheCell> apiCells = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.teddy.dev.api.cache.IApiCache#apis()
	 */
	@Override
	public Collection<ApiCacheCell> apis() {
		// TODO Auto-generated method stub
		List<ApiCacheCell> apis = new ArrayList<>();
		getApiCache().entrySet().forEach(_entry -> {
			if (!_entry.getValue().isEmpty()) {
				apis.add(_entry.getValue().get(0));
			}
		});
		return apis;
	}

	@Override
	public List<ApiCacheCell> get(String _key) {
		// TODO Auto-generated method stub
		return getApiCache().getOrDefault(_key, new ArrayList<>());
	}

	@Override
	public void add(ApiVO apiVO) {
		// TODO Auto-generated method stub
		String key = Utils.convertStringToBase64(apiVO.getType());
		apiCells.add(new ApiCacheCell(apiVO, key));
	}

	private Map<String, List<ApiCacheCell>> getApiCache() {
		if (null == apiCache) {
			apiCache = this.apiCells.stream()
					.collect(Collectors.groupingBy(_apiCell -> _apiCell.getKey()));
		}
		return apiCache;
	}
}
