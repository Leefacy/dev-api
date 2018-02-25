package com.teddy.httpClient.fgo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.teddy.httpClient.HttpClientDTO;

public class FgoClient {

	static String client(HttpClientDTO httpClientDTO, HttpHeaders requestHttpHeaders) {

		String result = "XOF";

		RestTemplate restTemplate = new RestTemplate();
		try {
			// 参数
			String _param = Optional.ofNullable(httpClientDTO.getParam()).orElse(null);
			// cookie
			// List<String> cookies =
			// Optional.ofNullable(httpClientDTO.getCookies())
			// .orElseGet(ArrayList::new);
			//
			// if (cookies.size() > 0) {
			// requestHttpHeaders.put(HttpHeaders.COOKIE,
			// httpClientDTO.getCookies());
			// }
			//
			// Optional.ofNullable(httpClientDTO.getContentType()).ifPresent(_contentType
			// -> {
			// requestHttpHeaders.add(HttpHeaders.CONTENT_TYPE, _contentType);
			// });
			// Optional.ofNullable(httpClientDTO.getAgent())
			// .ifPresent(_agent ->
			// requestHttpHeaders.add(HttpHeaders.USER_AGENT, _agent));

			if (httpClientDTO.getHttpMethod().equals(HttpMethod.GET)) {
				httpClientDTO.setUrl(httpClientDTO.getUrl() + "?" + _param);
				_param = null;
			}

			RequestEntity<String> requestEntity = new RequestEntity<>(_param, requestHttpHeaders,
					httpClientDTO.getHttpMethod(), new URI(httpClientDTO.getUrl()));
			ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity,
					String.class);
			result = responseEntity.getBody();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
}
