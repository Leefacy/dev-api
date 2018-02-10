package com.teddy.dev.client;

public class ApiShow {
	private String name;
	private String key;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param name
	 * @param key
	 */
	public ApiShow(String name, String key) {
		super();
		this.name = name;
		this.key = key;
	}

}
