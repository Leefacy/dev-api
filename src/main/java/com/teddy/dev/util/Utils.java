package com.teddy.dev.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import sun.misc.BASE64Encoder;

public class Utils {

	public static boolean isStringEmpty(String str) {
		return null == str || "".equals(str);
	}

	public static List<String> readFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		br.close();
		return lines;
	}

	public static List<String> readHtmlFile(File file) throws IOException {
		List<String> lines = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = br.readLine()) != null) {
			lines.add(line.replaceAll("\t", "&nbsp;"));
		}
		br.close();
		return lines;
	}

	public static String convertFileName(String name) {
		if (isStringEmpty(name)) {
			return name;
		}
		int start = name.lastIndexOf("/");
		String fileName = name.substring(start + 1);
		int end = fileName.indexOf(".", 0);
		if (end > 0) {
			return fileName.substring(0, end);
		} else {
			return fileName;
		}
	}

	public static String convertStringToBase64(String str) {
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(str.getBytes()).replaceAll("=", "");
	}

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = "101 家园";
		BASE64Encoder base64Encoder = new BASE64Encoder();
		System.out.println(base64Encoder.encode(s.getBytes()));

		String y = "102 用户";
		System.out.println(base64Encoder.encode(y.getBytes()));
	}
}
