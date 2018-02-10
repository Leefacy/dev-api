/**
 * 
 */
package com.teddy.dev.api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.teddy.dev.api.pojo.ApiVO;
import com.teddy.dev.api.pojo.ResultClzNode;
import com.teddy.dev.api.pojo.ResultMulNode;
import com.teddy.dev.api.pojo.ResultNode;
import com.teddy.dev.api.pojo.ResultSingleNode;

/**
 * @author Teddy.D.Share
 *
 */
public class ApiPrinter implements IApiOutPut {

	/**
	 * @param apiCache
	 */
	public ApiPrinter() {
		super();
	}

	private String printLoop(ResultNode<?> resultNode, boolean withComment) {

		String str = "";
		String comment = withComment ? "< // " + resultNode.getDesc() + ">" : "";
		if (resultNode instanceof ResultSingleNode) {
			str = String.format("\"%s\":\"%s\",%s", resultNode.getK(), resultNode.getV(), comment);
		}

		if (resultNode instanceof ResultMulNode) {
			if (null != resultNode.getK() && !"".equals(resultNode.getK())) {
				str = String.format("\"%s\":[%s%s],", resultNode.getK(), comment,
						printLoop(((ResultMulNode) resultNode).getV(), withComment));
			} else {
				str = "[" + printLoop(((ResultMulNode) resultNode).getV(), withComment) + "]";
			}
		}

		if (resultNode instanceof ResultClzNode) {
			if (null != resultNode.getK() && !"".equals(resultNode.getK())) {
				str = String.format("\"%s\":{%s%s},", resultNode.getK(), comment,
						((ResultClzNode) resultNode).getV().stream()
								.map(_resultNode -> printLoop(_resultNode, withComment))
								.collect(Collectors.joining()));
			} else {
				str = "{" + ((ResultClzNode) resultNode).getV().stream()
						.map(_resultNode -> printLoop(_resultNode, withComment))
						.collect(Collectors.joining()) + "}";
			}
		}
		return str;
	}

	private String print(ResultNode<?> resultNode) {
		return formatJson("{" + printLoop(resultNode, true) + "}");
		// return formatJson("{" + printLoopWithoutComment(resultNode) + "}");
	}

	/**
	 * 格式化
	 * 
	 * @param jsonStr
	 * @return
	 */
	private String formatJson(String jsonStr) {

		if (null == jsonStr || "".equals(jsonStr))
			return "";
		StringBuilder sb = new StringBuilder();
		char current = '\0'; // 当前字符
		char next = '\0'; // 下一个字符
		int index = 0; // 位置偏移量

		int length = jsonStr.length(); // 字符长度

		for (int i = 0; i < jsonStr.length(); i++) {
			next = jsonStr.charAt(Math.min(length - 1, i + 1));
			current = jsonStr.charAt(i);
			switch (current) {
			case '{':
			case '[':
				sb.append(current);
				index++; // 缩进
				if (next == '<') { // 有说明不换行
					break;
				}
				sb.append('\n');
				addIndentBlank(sb, index);
				break;
			case '<': // 说明开始
				break;
			case '>': // 说明结束
				if (next == '}' || next == ']') { // 换行逻辑交给收括号

				} else {
					sb.append('\n');
					addIndentBlank(sb, index);
				}
				break;
			case '}':
			case ']':
				sb.append('\n');
				index--;
				addIndentBlank(sb, index);
				sb.append(current);
				break;
			case ',':
				// 如果有说明，那么直接打印
				if (next == '<') { // 有说明,
					sb.append(current);
				} else if (next == '}' || next == ']') { // 末尾
				} else {
					sb.append(current);
					sb.append('\n');
					addIndentBlank(sb, index);
				}
				break;
			default:
				sb.append(current);
			}
		}
		return sb.toString();
	}

	/**
	 * 添加space
	 * 
	 * @param sb
	 * @param indent
	 */
	private void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}

	@Override
	public List<String> prints(List<ApiVO> apiVOs) {
		// TODO Auto-generated method stub
		int range = 1;
		List<String> lines = new ArrayList<>();
		for (ApiVO apiVO : apiVOs) {
			lines.addAll(print(apiVO, range++));
			lines.add("");
			lines.add("");
		}

		return lines;
	}

	public String print(ApiVO apiVO) {
		return print(apiVO, 0).stream().collect(Collectors.joining("\r\n"));
	}

	private List<String> print(ApiVO apiVO, int index) {
		List<String> lines = new ArrayList<>();

		lines.add(">>> " + index + "------" + apiVO.getMsg());
		lines.add("key : " + apiVO.getKey());
		lines.add("参数 : ");
		apiVO.getParams().forEach(_apiParam -> {
			lines.add("  " + _apiParam.getName() + ":" + _apiParam.getType() + "//"
					+ _apiParam.getDesc());
		});
		lines.add("返回结果 : ");
		lines.add(print(apiVO.getResult()));
		lines.add("错误结果 : ");
		apiVO.getErrorCodes().forEach(_apiError -> {
			lines.add("  " + _apiError.getCode() + ":" + _apiError.getMsg());
		});
		System.out.println(lines.stream().collect(Collectors.joining("\r\n")));
		return lines;
	}

	@Override
	public Map<String, List<String>> printsMul(List<ApiVO> apivos) {
		// TODO Auto-generated method stub
		Map<String, List<ApiVO>> maps = apivos.stream()
				.collect(Collectors.groupingBy(_apiVO -> _apiVO.getType()));

		Map<String, List<String>> result = new HashMap<>();

		for (Entry<String, List<ApiVO>> entry : maps.entrySet()) {
			result.put(entry.getKey(), prints(entry.getValue()));
		}
		return result;
	}

	@Override
	public void print(List<ApiVO> apiVOs, String path) {
		// TODO Auto-generated method stub
		try {
			for (Entry<String, List<String>> entry : printsMul(apiVOs).entrySet()) {
				String fileName = path + entry.getKey() + ".txt";
				BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
				for (String line : entry.getValue()) {
					bw.write(line);
					bw.newLine();
				}
				bw.flush();
				bw.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
