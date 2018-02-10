/**
 * TODO
 */
package com.teddy.dev.api.pojo;

import java.util.List;

/**
 * @author Teddy.D.Share 2018年1月29日
 */
public class ApiVO {

	private String type; // 接口所属类型

	private String msg; // 接口说明
	private String key; // 接口的key
	private List<ApiParam> params; // 接口参数
	private ResultNode<?> result; // 返回结果
	private List<ApiError> errorCodes; // 异常情况

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<ApiParam> getParams() {
		return params;
	}

	public void setParams(List<ApiParam> params) {
		this.params = params;
	}

	public ResultNode<?> getResult() {
		return result;
	}

	public void setResult(ResultNode<?> result) {
		this.result = result;
	}

	public List<ApiError> getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(List<ApiError> errorCodes) {
		this.errorCodes = errorCodes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ApiVO [type=" + type + ", msg=" + msg + ", key=" + key + ", params=" + params + ", result=" + result
				+ ", errorCodes=" + errorCodes + "]";
	}
}
