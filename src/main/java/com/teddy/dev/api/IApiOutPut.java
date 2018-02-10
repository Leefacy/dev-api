package com.teddy.dev.api;

import java.util.List;
import java.util.Map;

import com.teddy.dev.api.pojo.ApiVO;

public interface IApiOutPut {

	/**
	 * 打印一堆
	 * 
	 * @param apiVOs
	 * @return
	 */
	public List<String> prints(List<ApiVO> apiVOs);

	/**
	 * 打印成多份
	 * 
	 * @param apivos
	 * @return
	 */
	public Map<String, List<String>> printsMul(List<ApiVO> apivos);

	/**
	 * 输出到文件
	 * 
	 * @param apiVOs
	 * @param path
	 */
	public void print(List<ApiVO> apiVOs, String path);
}
