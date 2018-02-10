/**
 * TODO
 */
package com.teddy.dev.api;

import java.io.File;
import java.util.List;

import com.teddy.dev.util.ClzUtil;

/**
 * API 配置
 * 
 * @author Teddy.D.Share 2018年1月30日
 */
public class ApiConfiguration {

	private String path = "F://data/webgame/server/lskg-gameserver"; // 工程路径

	public static String srcPath = "F://data/webgame/server/lskg-gameserver/src/main/java/"; //

	private String outPath = "/output/"; // 输出

	private List<File> files;

	public String getOutPath() {
		return outPath;
	}

	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		srcPath = path + "/src/main/java/";
	}

	public List<File> getFiles() {
		return files;
	}

	public ApiConfiguration(String path, String outPath) {
		super();
		// TODO Auto-generated constructor stub
		this.outPath = outPath;
		this.path = path;
		this.files = ClzUtil.scanPath(this.path);
	}
}
