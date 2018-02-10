package com.teddy.dev.api;

import java.io.File;
import java.util.List;

/**
 * @author Teddy.D.Share
 */
public interface IApiFilter<T> {

	/**
	 * 文件中的api
	 * 
	 * @param file
	 * @return
	 */
	public List<T> doFilter(File file);
}
