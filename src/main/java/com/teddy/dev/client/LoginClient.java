package com.teddy.httpClient.fgo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.teddy.httpClient.HttpClientDTO;

public class LoginClient extends FgoClient {

	/**
	 * 网络配置
	 * 
	 * @return
	 */
	public static String netWorkConfig() {

		HttpHeaders headers = new HttpHeaders();

		headers.add("User-Agent", "fatego/16 CFNetwork/894 Darwin/17.4.0");
		headers.add("Accept-Language", "zh-cn");
		headers.add("Accept-Encoding", "br, gzip, deflate");
		headers.add("X-Unity-Version", "5.3.3f1");

		HttpClientDTO httpClientDTO = new HttpClientDTO();
		httpClientDTO.setHttpMethod(HttpMethod.GET);
		httpClientDTO.setParam("t=" + System.currentTimeMillis() / 1000);
		httpClientDTO.setUrl(
				"https://line1-s1-ios-fate.bilibiligame.net/rongame_beta/rgfate/60_member/network/network_config_ios_1.17.1.json");

		String result = client(httpClientDTO, headers);
		System.out.println(result);
		return result;
	}

	public static String login() {
		HttpHeaders headers = new HttpHeaders();

		headers.add("User-Agent", "fatego/16 CFNetwork/894 Darwin/17.4.0");
		headers.add("Accept-Language", "zh-cn");
		headers.add("Accept-Encoding", "br, gzip, deflate");
		headers.add("X-Unity-Version", "5.3.3f1");

		HttpClientDTO httpClientDTO = new HttpClientDTO();
		httpClientDTO.setHttpMethod(HttpMethod.GET);
		httpClientDTO.setParam("t=" + System.currentTimeMillis() / 1000);
		httpClientDTO.setUrl(
				"https://line1-s1-ios-fate.bilibiligame.net/rongame_beta/rgfate/60_member/network/network_config_ios_1.17.1.json");

		String result = client(httpClientDTO, headers);
		System.out.println(result);
		return result;
	}

	public static void main(String[] args) {
		netWorkConfig();
	}
}
