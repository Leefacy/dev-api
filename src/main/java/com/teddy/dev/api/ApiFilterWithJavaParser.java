package com.teddy.dev.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.teddy.dev.api.pojo.MethodVO;

public class ApiFilterWithJavaParser extends GenericVisitorAdapter<Boolean, String> implements IApiFilter<MethodVO> {

	/*
	 * 接口方法过滤
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.github.
	 * javaparser.ast.body.MethodDeclaration, java.lang.Object)
	 */
	@Override
	public Boolean visit(MethodDeclaration methodDeclaration, String args) {
		return methodDeclaration.getModifiers().contains(Modifier.PUBLIC) && methodDeclaration.getAnnotations().stream()
				.filter(_annotationExpr -> _annotationExpr.accept(this, args)).findAny().isPresent();
	}

	/*
	 * 注解判断
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.github.
	 * javaparser.ast.expr.NormalAnnotationExpr, java.lang.Object)
	 */
	@Override
	public Boolean visit(NormalAnnotationExpr normalAnnotationExpr, String args) {
		return args.equals(normalAnnotationExpr.getNameAsString());
	}

	/*
	 * 注解判断
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.github.
	 * javaparser.ast.expr.MarkerAnnotationExpr, java.lang.Object)
	 */
	@Override
	public Boolean visit(MarkerAnnotationExpr markerAnnotationExpr, String args) {
		return args.equals(markerAnnotationExpr.getNameAsString());
	}

	/*
	 * 注解判断
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.github.
	 * javaparser.ast.expr.SingleMemberAnnotationExpr, java.lang.Object)
	 */
	@Override
	public Boolean visit(SingleMemberAnnotationExpr singleMemberAnnotationExpr, String args) {
		return args.equals(singleMemberAnnotationExpr.getNameAsString());
	}

	/*
	 * 类名判断
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.github.
	 * javaparser.ast.type.ClassOrInterfaceType, java.lang.Object)
	 */
	@Override
	public Boolean visit(ClassOrInterfaceType classOrInterfaceType, String args) {
		return args.equals(classOrInterfaceType.asString());
	}

	/*
	 * 类
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.github.
	 * javaparser.ast.body.ClassOrInterfaceDeclaration, java.lang.Object)
	 */
	@Override
	public Boolean visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, String args) {
		boolean isService = classOrInterfaceDeclaration.getAnnotations().stream()
				.filter(_annotationExpr -> _annotationExpr.accept(this, ApiConstant.SERVICE)).findAny().isPresent();
		boolean isGateBiz = classOrInterfaceDeclaration.getImplementedTypes().stream()
				.filter(_classOrInterfaceType -> _classOrInterfaceType.accept(this, ApiConstant.GATE_API)).findAny()
				.isPresent();
		return isService && isGateBiz;
	}

	@Override
	public List<MethodVO> doFilter(File file) {
		// TODO Auto-generated method stub

		List<MethodVO> methods = new ArrayList<>();

		if (file.getName().endsWith(".java")) {
			try {
				CompilationUnit compilationUnit = JavaParser.parse(file);
				compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(_classOrInterfaceDeclaration -> {
					boolean isBizClz = _classOrInterfaceDeclaration.accept(this, "");
					if (isBizClz) {
						String type = Optional
								.ofNullable(((NodeWithJavadoc<?>) _classOrInterfaceDeclaration).getJavadoc().get())
								.map(_javaDoc -> _javaDoc.getDescription().toText()).orElse("");
						_classOrInterfaceDeclaration.findAll(MethodDeclaration.class).forEach(_methodDeclaration -> {
							boolean isBizMethod = _methodDeclaration.accept(this, ApiConstant.GATE_METHOD);
							if (isBizMethod) {
								MethodVO methodVO = new MethodVO();
								methodVO.setMethodDeclaration(_methodDeclaration);
								methodVO.setType(type);
								methods.add(methodVO);
							}
						});
					}
				});
			} catch (FileNotFoundException e) {
				System.out.println(file.getName());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return methods;
	}
}
