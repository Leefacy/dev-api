/**
 * TODO
 */
package com.teddy.dev.api.pojo;

/**
 * @author Teddy.D.Share 2018年1月31日
 */
public class ApiError {

	private int code; // 错误码
	private String msg; // 错误内容

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ApiError(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public ApiError(String error) {
		super();
		if (null != error && !"".equals(error)) {
			String[] e = error.split(":");
			this.code = Integer.parseInt(e[0]);
			this.msg = e[1];
		}
	}
}
