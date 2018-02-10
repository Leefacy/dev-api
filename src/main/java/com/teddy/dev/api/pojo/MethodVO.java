package com.teddy.dev.api.pojo;

import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodVO {

	private MethodDeclaration methodDeclaration; // 方法
	private String type; // 类说明，方法所属

	public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}

	public void setMethodDeclaration(MethodDeclaration methodDeclaration) {
		this.methodDeclaration = methodDeclaration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
