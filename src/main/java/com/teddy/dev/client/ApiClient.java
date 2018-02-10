package com.teddy.dev.client;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teddy.dev.App;
import com.teddy.dev.api.cache.ApiCache;

/**
 * @author Teddy.D.Share 2018年2月8日
 */
@Controller
@RequestMapping("/api")
public class ApiClient {

	private ApiCache apiCache = (ApiCache) App.appConfiguration.getApiCache();

	// @GetMapping("/list1")
	// public Collection<ApiShow> listApi() {
	// return apiCache.listApi();
	// }

	@RequestMapping("/list")
	public String list(ModelMap map) {
		// List<ApiShow> apis = new ArrayList<>();
		// apis.add(new ApiShow("F://output/101 兑换.txt", new ApiVO()));
		// apis.add(new ApiShow("F://output/102 家园.txt", new ApiVO()));
		map.addAttribute("apis",
				apiCache.apis().stream()
						.map(_apiCacheCell -> new ApiShow(_apiCacheCell.getApiVO().getType(),
								_apiCacheCell.getKey()))
						.collect(Collectors.toList()));
		return "list";
	}

	@RequestMapping("/say")
	public String say(ModelMap map) {

		return "hello";
	}

	@GetMapping("/get")
	public String getApi(@RequestParam("key") String key, ModelMap map) throws IOException {
		// map.addAttribute("lines", Utils.readFile(new
		// File(apiCache.getApiName(name).getPath())));
		// map.addAttribute("lines", Utils.readFile(new File("F://output/102
		// 家园.txt")));

		map.addAttribute("apis", apiCache.get(key));
		return "api";
	}

	// @GetMapping("/")
	// public String listActs(Model model) throws IOException {
	// model.addAttribute("acts", "123");
	// return "hello";
	// }
	//
	// @RequestMapping("/hello")
	// public String hello(ModelMap map) {
	// map.addAttribute("say", "Hello!");
	// return "hello.html";
	// }
	//
	@GetMapping("/download")
	public ResponseEntity<Resource> getGoodsXml(@RequestParam("name") String name)
			throws MalformedURLException {
		Resource resource = new FileSystemResource(new File(name));
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
	}
	//
	// @GetMapping("/get")
	// public List<String> getApi(@RequestParam("name") String name) throws
	// IOException {
	// return Utils.readFile(new File(apiCache.getApiName(name).getPath()));
	// }

}
