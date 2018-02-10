/**
 * TODO
 */
package com.teddy.dev.api.pojo;

/**
 * 字段基础单元
 * 
 * @author Teddy.D.Share 2018年1月30日
 */
public class ApiEntry {

	private String name; // key
	private String type; // type
	private String desc; // desc

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 
	 */
	public ApiEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param type
	 * @param desc
	 */
	public ApiEntry(String name, String type, String desc) {
		super();
		this.name = name;
		this.type = type;
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "ApiEntry [name=" + name + ", type=" + type + ", desc=" + desc + "]";
	}
}
