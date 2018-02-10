package com.teddy.dev;

import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.teddy.dev.api.ApiGenerator;
import com.teddy.dev.api.cache.ApiCache;
import com.teddy.dev.api.cache.ApiCacheCell;
import com.teddy.dev.api.pojo.ApiVO;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

	public static final AppConfiguration<ApiCacheCell, String> appConfiguration = new AppConfiguration<ApiCacheCell, String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Stream.of(args).forEach(System.out::println);
//		String path = "F://data/webgame/server/lskg-gameserver";
//		ApiGenerator<ApiCacheCell, String> apiGenerator = new ApiGenerator<ApiCacheCell, String>(
//				path, new ApiCache());
//		apiGenerator.generate();
//		appConfiguration.setApiConfiguration(apiGenerator.getApiConfiguration());
//		appConfiguration.setApiCache(apiGenerator.getApiCache());

		SpringApplication.run(App.class, args);
	}

}
