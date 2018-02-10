package com.teddy.dev.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.teddy.dev.api.pojo.ApiError;
import com.teddy.dev.api.pojo.ApiParam;
import com.teddy.dev.api.pojo.ApiVO;
import com.teddy.dev.api.pojo.MethodVO;
import com.teddy.dev.api.pojo.ResultNode;
import com.teddy.dev.api.visitor.ReturnVisitor;
import com.teddy.dev.api.visitor.VisitorParam;
import com.teddy.dev.util.ParseUtils;
import com.teddy.dev.util.Utils;

/**
 * 通过javaparser转换
 * 
 * @author Teddy.D.Share
 */
public class ApiTansformWithJavaParser implements IApiTransform {

	private IApiFilter<MethodVO> apiFilter = new ApiFilterWithJavaParser();
	private ReturnVisitor returnVisitor = new ReturnVisitor();

	@Override
	public List<ApiVO> tansform(List<File> files) {
		// TODO Auto-generated method stub
		List<ApiVO> apiVOs = new ArrayList<>();

		for (File file : files) {
			List<MethodVO> methods = apiFilter.doFilter(file);
			for (MethodVO methodVO : methods) {

				System.out.println(file.getName() + "."
						+ methodVO.getMethodDeclaration().getNameAsString() + " found");

				MethodDeclaration methodDeclaration = methodVO.getMethodDeclaration();
				ApiMethodDoc apiMethodDoc = convertMethodDoc(methodDeclaration);
				List<ApiParam> params = convertMethodParams(methodDeclaration,
						apiMethodDoc.getParams());
				List<ApiError> errors = new ArrayList<>();
				Optional.ofNullable(apiMethodDoc.getError()).ifPresent(_strs -> {
					Stream.of(_strs).filter(_s -> !Utils.isStringEmpty(_s))
							.forEach(_s -> errors.add(new ApiError(_s)));
				});
				ApiUri apiUri = convertMethodUri(methodDeclaration);
				VisitorParam param = new VisitorParam();
				param.setDesc(ParseUtils.comment(methodDeclaration));
				param.setName(apiUri.getKey());
				param.setJavaFile(file);
				ResultNode<?> resultNode = methodDeclaration.accept(returnVisitor, param);
				ApiVO apiVO = new ApiVO();
				apiVO.setResult(resultNode);
				apiVO.setParams(params);
				apiVO.setMsg(apiMethodDoc.getMsg());
				apiVO.setKey(apiUri.getKey());
				apiVO.setErrorCodes(errors);
				apiVO.setType(methodVO.getType());
				apiVOs.add(apiVO);
			}
		}
		return apiVOs;
	}

	/**
	 * 解析方法说明
	 * 
	 * @param _mth
	 * @return
	 */
	private ApiMethodDoc convertMethodDoc(MethodDeclaration mth) {
		ApiMethodDoc apiMethodDoc = new ApiMethodDoc();
		mth.getJavadoc().ifPresent(_javaDoc -> {
			List<JavadocBlockTag> listOfParam = new ArrayList<>();
			List<JavadocBlockTag> listOfError = new ArrayList<>();

			_javaDoc.getBlockTags().forEach(_tag -> {
				if (_tag.getType().equals(JavadocBlockTag.Type.PARAM)) {
					listOfParam.add(_tag);
				}
				if (_tag.getTagName().equals("error")) {
					listOfError.add(_tag);
				}
			});

			int size = listOfParam.size();
			if (size > 0) {
				String[] params = new String[size];
				for (int i = 0; i < listOfParam.size(); i++) {
					params[i] = listOfParam.get(i).getContent().toText();
				}
				apiMethodDoc.setParams(params); // 参数说明
			}
			if (listOfError.size() > 0) {
				apiMethodDoc.setError(listOfError.stream().map(_tag -> _tag.getContent().toText())
						.toArray(_i -> new String[_i]));
			}
			apiMethodDoc.setMsg(_javaDoc.getDescription().toText()); // 接口说明
			apiMethodDoc.setRet(_javaDoc.getBlockTags().stream()
					.filter(_tag -> _tag.getType().equals(JavadocBlockTag.Type.RETURN)).findFirst()
					.map(_tag -> _tag.getContent().toText()).orElse("")); // 返回说明
		});
		return apiMethodDoc;
	}

	/**
	 * 解析uri
	 * 
	 * @param ann
	 * @return
	 */
	private ApiUri convertMethodUri(MethodDeclaration mth) {
		ApiUri apiUri = new ApiUri();
		mth.getAnnotationByName(ApiConstant.GATE_METHOD)
				.ifPresent(ann -> ann.ifNormalAnnotationExpr(_ann -> {
					apiUri.setKey(_ann.getPairs().stream()
							.filter(_memberValuePair -> _memberValuePair.getNameAsString()
									.equals(ApiConstant.FEATURE_OF_GATE_BIZ))
							.map(_memberValuePair -> _memberValuePair.getValue()
									.asStringLiteralExpr().getValue())
							.findFirst().orElse("未知的key " + ann.toString()));
				}));
		return apiUri;
	}

	/**
	 * 解析参数
	 * 
	 * @param mth
	 * @param paramsMsg
	 * @return
	 */
	private List<ApiParam> convertMethodParams(MethodDeclaration mth, String[] paramsMsg) {

		List<ApiParam> apiParams = new ArrayList<>();

		mth.getParameters().stream().forEach(_parameter -> {
			_parameter.getAnnotationByName(ApiConstant.GATE_PARAM).ifPresent(_ann -> {
				_ann.asNormalAnnotationExpr().getPairs().stream()
						.filter(_memberValuePair -> _memberValuePair.getNameAsString()
								.equals(ApiConstant.KEY_OF_GATE_PARAM))
						.findFirst().ifPresent(_memberValuePair -> {
							_memberValuePair.getValue().ifStringLiteralExpr(_exp -> {
								ApiParam apiParam = new ApiParam();
								apiParam.setName(_memberValuePair.getValue().asStringLiteralExpr()
										.getValue());
								apiParam.setType(_parameter.getType().asString());
								apiParams.add(apiParam);
							});

							_memberValuePair.getValue().ifFieldAccessExpr(_exp -> {
								ApiParam apiParam = new ApiParam();
								apiParam.setName(_memberValuePair.getValue().asFieldAccessExpr()
										.getNameAsString());
								apiParam.setType(_parameter.getType().asString());
								apiParams.add(apiParam);
							});
						});
			});
		});

		for (int i = 0; i < paramsMsg.length && i < apiParams.size(); i++) {
			apiParams.get(i).setDesc(paramsMsg[i]);
		}

		return apiParams.stream().filter(_param -> !(_param.getName().equals("RetUser")
				|| _param.getName().contains("RET_USER_KEY"))).collect(Collectors.toList());
	}

	/**
	 * 方法体 文档
	 * 
	 * @author Teddy.D.Share 2018年2月5日
	 */
	public class ApiMethodDoc {
		private String msg; // 方法体说明
		private String[] params; // 参数列表说明
		private String ret; // 返回说明
		private String[] error; // 错误列表

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String[] getParams() {
			return params;
		}

		public void setParams(String[] params) {
			this.params = params;
		}

		public String getRet() {
			return ret;
		}

		public void setRet(String ret) {
			this.ret = ret;
		}

		public String[] getError() {
			return error;
		}

		public void setError(String[] error) {
			this.error = error;
		}
	}

	/**
	 * api uri
	 * 
	 * @author Teddy.D.Share 2018年2月5日
	 */
	public class ApiUri {

		private String key; // uri
		private String requestMethod; // 请求方式

		public String getRequestMethod() {
			return requestMethod;
		}

		public void setRequestMethod(String requestMethod) {
			this.requestMethod = requestMethod;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}
}
