/**
 * 
 */
package com.teddy.dev.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.teddy.dev.api.cache.ApiCache;
import com.teddy.dev.api.cache.ApiCacheCell;
import com.teddy.dev.api.cache.IApiCache;
import com.teddy.dev.api.pojo.ApiVO;

/**
 * @author Teddy.D.Share
 *
 */
public class ApiGenerator<T, K> {

	private ApiConfiguration apiConfiguration; // 配置

	private IApiOutPut apiOutPut; // 输出
	private IApiTransform apiTransform; // 转化
	private IApiCache<T, K> apiCache; // 缓存

	public ApiConfiguration getApiConfiguration() {
		return apiConfiguration;
	}

	public IApiOutPut getApiOutPut() {
		return apiOutPut;
	}

	public IApiTransform getApiTransform() {
		return apiTransform;
	}

	public IApiCache<T, K> getApiCache() {
		return apiCache;
	}

	public ApiGenerator(ApiConfiguration apiConfiguration, IApiOutPut apiOutPut,
			IApiTransform apiTransform, IApiCache<T, K> apiCache) {
		super();
		this.apiConfiguration = apiConfiguration;
		this.apiOutPut = apiOutPut;
		this.apiTransform = apiTransform;
		this.apiCache = apiCache;
	}

	public ApiGenerator(ApiConfiguration apiConfiguration, IApiCache<T, K> apiCache) {
		this(apiConfiguration, new ApiPrinter(), new ApiTansformWithJavaParser(), apiCache);
	}

	public ApiGenerator(String path, String outPath, IApiCache<T, K> apiCache) {
		this(new ApiConfiguration(path, outPath), apiCache);
	}

	public ApiGenerator(String path, IApiCache<T, K> apiCache) {
		this(path, ApiConstant.API_OUT_PATH, apiCache);
	}

	public ApiGenerator(IApiCache<T, K> apiCache) {
		this("F://data/webgame/server/lskg-gameserver", apiCache);
	}

	public void generate() {
		List<File> files = apiConfiguration.getFiles();
		List<ApiVO> apiVOs = this.apiTransform.tansform(files);
		for (ApiVO apiVO : apiVOs) {
			apiCache.add(apiVO);
		}
		this.apiOutPut.print(apiVOs, apiConfiguration.getOutPath());
	}

	/**
	 * @param args
	 * @throws IOException
	 * @error
	 */
	public static void main(String[] args) throws IOException {
		ApiGenerator<ApiCacheCell, String> apiGenerator = new ApiGenerator<ApiCacheCell, String>(
				new ApiCache());
		apiGenerator.generate();
	}

}
