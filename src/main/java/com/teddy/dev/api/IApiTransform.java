package com.teddy.dev.api;

import java.io.File;
import java.util.List;

import com.teddy.dev.api.pojo.ApiVO;

/**
 * @author Teddy.D.Share
 *
 */
public interface IApiTransform {

	public List<ApiVO> tansform(List<File> files);
}
