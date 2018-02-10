package com.teddy.dev.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;
import com.teddy.dev.api.ApiConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class ParseUtils {

	/**
	 * search File of className in the java file
	 *
	 * @param inJavaFile
	 * @param className
	 * @return
	 */
	public static File searchJavaFile(File inJavaFile, String className) {
		File file = searchJavaFileInner(inJavaFile, className);
		if (file == null) {
			throw new RuntimeException("cannot find java file , in java file : " + inJavaFile.getAbsolutePath()
					+ ", className : " + className);
		}
		return file;
	}

	private static CompilationUnit compilationUnit(File file) {
		try {
			return JavaParser.parse(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("java file not exits , file path : " + file.getAbsolutePath());
		}
	}

	private static File searchJavaFileInner(File inJavaFile, String className) {
		CompilationUnit compilationUnit = compilationUnit(inJavaFile);
		String[] cPaths;
		Optional<ImportDeclaration> idOp = compilationUnit.getImports().stream()
				.filter(im -> im.getNameAsString().endsWith(className)).findFirst();

		// found in import
		if (idOp.isPresent()) {
			cPaths = idOp.get().getNameAsString().split("\\.");
			return backTraceJavaFileByName(cPaths);
		}

		// inner class
		if (getInnerClassNode(compilationUnit, className).isPresent()) {
			return inJavaFile;
		}

		cPaths = className.split("\\.");

		// current directory
		if (cPaths.length == 1) {

			File[] javaFiles = inJavaFile.getParentFile().listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.equals(className + ".java");
				}
			});

			if (javaFiles != null && javaFiles.length == 1) {
				return javaFiles[0];
			}

		} else {

			final String firstPath = cPaths[0];
			// same package inner class
			File[] javaFiles = inJavaFile.getParentFile().listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					int i = name.lastIndexOf(".java");
					if (i == -1) {
						return false;
					}
					return name.substring(0, i).equals(firstPath);
				}
			});

			if (javaFiles != null && javaFiles.length > 0) {
				File javaFile = javaFiles[0];
				if (getInnerClassNode(compilationUnit(javaFile), className).isPresent()) {
					return javaFile;
				}
			}
		}

		// maybe a complete class name
		File javaFile = backTraceJavaFileByName(cPaths);
		if (javaFile != null) {
			return javaFile;
		}

		// .* at import
		NodeList<ImportDeclaration> importDeclarations = compilationUnit.getImports();
		if (importDeclarations.isNonEmpty()) {
			for (ImportDeclaration importDeclaration : importDeclarations) {
				if (importDeclaration.toString().contains(".*")) {
					String packageName = importDeclaration.getNameAsString();
					cPaths = (packageName + "." + className).split("\\.");
					javaFile = backTraceJavaFileByName(cPaths);
					if (javaFile != null) {
						break;
					}
				}
			}
		}

		return javaFile;
	}

	/**
	 * get inner class node
	 * 
	 * @param compilationUnit
	 * @param className
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Optional<TypeDeclaration> getInnerClassNode(CompilationUnit compilationUnit, String className) {
		return compilationUnit.findAll(TypeDeclaration.class).stream()
				.filter(c -> c instanceof ClassOrInterfaceDeclaration || c instanceof EnumDeclaration)
				.filter(c -> className.endsWith(c.getNameAsString())).findFirst();
	}

	private static File backTraceJavaFileByName(String[] cPaths) {
		if (cPaths.length == 0) {
			return null;
		}
		String javaFilePath = ApiConfiguration.srcPath + Stream.of(cPaths).collect(Collectors.joining("/")) + ".java";
		File javaFile = new File(javaFilePath);
		if (javaFile.exists() && javaFile.isFile()) {
			return javaFile;
		} else {
			return backTraceJavaFileByName(Arrays.copyOf(cPaths, cPaths.length - 1));
		}
	}

	public static String comment(Node node) {
		return comment(node, _s -> _s);
	}

	public static String comment(Node node, Function<String, String> fun) {
		return Optional.ofNullable(node.getComment().get()).map(_comment -> {
			String content = "";
			try {
				content = _comment.asBlockComment().getContent();
			} catch (Exception e) {
				// TODO: handle exception
				try {
					_comment.asJavadocComment();
					content = Optional.ofNullable(((NodeWithJavadoc<?>) node).getJavadoc().get())
							.map(_javaDoc -> _javaDoc.getDescription().toText()).orElse("");
				} catch (Exception e2) {
					// TODO: handle exception
					content = _comment.asLineComment().getContent();
				}
			}
			return fun.apply(content);
		}).orElse("");
	}
}
