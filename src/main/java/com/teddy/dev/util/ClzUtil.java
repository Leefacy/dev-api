/**
 * TODO
 */
package com.teddy.dev.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.teddy.dev.api.pojo.EnumClzType;

/**
 * @author Teddy.D.Share 2018年1月29日
 */
public class ClzUtil {

	/**
	 * java文件对应的类名
	 * 
	 * @param file
	 * @return
	 */
	public static String getJavaClzName(File file) {
		String fileName = file.getName();
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	/**
	 * Boolean Byte Character Double Enum Float Integer Long Short String Void
	 */
	private static final String[] baseClass = { "String", "Integer", "Long", "Boolean", "Byte", "Character", "Double",
			"Float", "Short", "Void" };

	public static boolean isBaseType(String clzName) {
		return Stream.of(baseClass).filter(_s -> _s.equals(clzName)).findFirst().isPresent();
	}

	/**
	 * 分离出类型字段
	 * 
	 * @param clzName
	 * @return
	 */
	public static String convertClzName(String clzName) {
		String[] cPaths = clzName.split("\\.");
		String genericType = cPaths[cPaths.length - 1];
		int genericLeftIndex = genericType.indexOf("<");
		String rawType = genericLeftIndex != -1 ? genericType.substring(0, genericLeftIndex) : genericType;
		return rawType;
	}

	/**
	 * 类类型
	 * 
	 * @param clzName
	 * @return
	 */
	public static EnumClzType convertClzType(String clzName) {

		String rawType = convertClzName(clzName);

		if (isBaseType(rawType)) {
			return EnumClzType.BASE;
		} else {
			String rawName = "java.util." + rawType;
			try {
				Class<?> clz = Class.forName(rawName);
				if (Map.class.isAssignableFrom(clz)) {
					return EnumClzType.MAP;
				} else if (Collection.class.isAssignableFrom(clz)) {
					return EnumClzType.COLLECTION;
				} else {
					return EnumClzType.UNKNOW;
				}
			} catch (ClassNotFoundException e) {
				return EnumClzType.UNKNOW;
			}
		}

	}

	/**
	 * 遍历root下的所有文件
	 *
	 * @param root
	 * @return
	 */
	public static List<File> scanPath(String root) {
		List<File> files = new ArrayList<>();
		scanPathLoop(files, new File(root));
		return files;
	}

	private static void scanPathLoop(List<File> files, File file) {
		if (file.isDirectory()) {
			Stream.of(file.listFiles()).forEach(_f -> scanPathLoop(files, _f));
		} else {
			files.add(file);
		}
	}
}
