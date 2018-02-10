package com.teddy.dev.api.visitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.teddy.dev.api.pojo.EnumClzType;
import com.teddy.dev.api.pojo.ResultClzNode;
import com.teddy.dev.api.pojo.ResultMulNode;
import com.teddy.dev.api.pojo.ResultNode;
import com.teddy.dev.api.pojo.ResultSingleNode;
import com.teddy.dev.util.ClzUtil;
import com.teddy.dev.util.ParseUtils;

public class ReturnVisitor extends GenericVisitorAdapter<ResultNode<?>, VisitorParam> {

	@Override
	public ResultNode<?> visit(MethodDeclaration methodDeclaration, VisitorParam param) {
		return methodDeclaration.getType().accept(this, param);
	}

	/*
	 * 返回是类 <p>(non-Javadoc)
	 * 
	 * @see com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.
	 * github.javaparser.ast.type.ClassOrInterfaceType, java.lang.Object)
	 */
	@Override
	public ResultNode<?> visit(ClassOrInterfaceType classOrInterfaceType, VisitorParam param) {

		String typeName = classOrInterfaceType.asString();
		String clzName = ClzUtil.convertClzName(typeName);
		EnumClzType clzType = ClzUtil.convertClzType(clzName);

		switch (clzType) {
		case BASE:
			ResultSingleNode resultSingleNode = new ResultSingleNode();
			resultSingleNode.setV(typeName);
			resultSingleNode.setDesc(param.getDesc());
			resultSingleNode.setK(param.getName());
			return resultSingleNode;
		case COLLECTION:
			List<Node> childs = classOrInterfaceType.getChildNodes();
			ResultMulNode resultMulNode = new ResultMulNode();
			VisitorParam subParam = new VisitorParam();
			subParam.setJavaFile(param.getJavaFile());
			resultMulNode.setV(childs.get(1).accept(this, subParam));
			resultMulNode.setDesc(param.getDesc());
			resultMulNode.setK(param.getName());
			return resultMulNode;
		case MAP:
			// no support
			break;

		case UNKNOW:
			File resultFile = ParseUtils.searchJavaFile(param.getJavaFile(), typeName);
			try {
				CompilationUnit compilationUnit = JavaParser.parse(resultFile);
				ClassOrInterfaceDeclaration classOrInterfaceDeclaration = compilationUnit.getClassByName(typeName)
						.get();
				VisitorParam visitorParam = new VisitorParam();
				visitorParam.setJavaFile(resultFile);

				ResultClzNode resultClzNode = new ResultClzNode();
				resultClzNode.setV(classOrInterfaceDeclaration.getFields().stream()
						.map(_fieldDeclaration -> _fieldDeclaration.accept(this, visitorParam))
						.collect(Collectors.toList()));
				resultClzNode.setDesc(param.getDesc());
				resultClzNode.setK(param.getName());
				return resultClzNode;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

		return null;
	}

	@Override
	public ResultNode<?> visit(FieldDeclaration fieldDeclaration, VisitorParam param) {
		String comment = ParseUtils.comment(fieldDeclaration);
		NodeList<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
		for (VariableDeclarator variableDeclarator : variableDeclarators) {
			param.setDesc(comment);
			param.setName(variableDeclarator.getNameAsString());
			return fieldDeclaration.getElementType().accept(this, param);
		}
		return null;
	}

	/*
	 * 返回为int,long,boolean等<p> (non-Javadoc)
	 * 
	 * @see com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.
	 * github.javaparser.ast.type.PrimitiveType, java.lang.Object)
	 */
	@Override
	public ResultNode<?> visit(PrimitiveType primitiveType, VisitorParam param) {
		ResultSingleNode resultSingleNode = new ResultSingleNode();
		resultSingleNode.setV(primitiveType.asString());
		resultSingleNode.setDesc(param.getDesc());
		resultSingleNode.setK(param.getName());
		return resultSingleNode;
	}

	/*
	 * 返回是数组<p> (non-Javadoc)
	 * 
	 * @see com.github.javaparser.ast.visitor.GenericVisitorAdapter#visit(com.
	 * github.javaparser.ast.type.ArrayType, java.lang.Object)
	 */
	@Override
	public ResultNode<?> visit(ArrayType arrayType, VisitorParam param) {
		Type component = arrayType.getComponentType();
		ResultNode<ResultNode<?>> resultMulNode = new ResultMulNode();
		resultMulNode.setDesc(param.getDesc());
		resultMulNode.setK(param.getName());
		resultMulNode.setV(component.accept(this, param));
		return resultMulNode;
	}
}
