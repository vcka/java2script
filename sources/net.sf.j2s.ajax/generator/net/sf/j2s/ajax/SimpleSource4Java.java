/*******************************************************************************
 * Copyright (c) 2012 java2script.org and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Zhou Renjian - initial API and implementation
 *******************************************************************************/

package net.sf.j2s.ajax;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.j2s.ajax.SimpleSerializable;
import net.sf.j2s.ajax.annotation.SimpleComment;
import net.sf.j2s.ajax.annotation.SimpleIn;
import net.sf.j2s.ajax.annotation.SimpleInOut;
import net.sf.j2s.ajax.annotation.SimpleOut;

public class SimpleSource4Java {
	
	static String folder = "Project";
	static String author = "Author";
	static String company = "Company";

	static Set<Class<?>> eventClasses = new HashSet<Class<?>>();
	
	@SuppressWarnings("deprecation")
	public static String generateSourceFromInterface(Class<?> interfaceClazz) {
		StringBuilder source = new StringBuilder();
		Date date = new Date();
		source.append("/**\r\n");
		source.append(" * Generated by Java2Script.\r\n");
		source.append(" * Copyright (c) ");
		source.append(date.getYear() + 1900);
		source.append(" ");
		source.append(company);
		source.append(". All rights reserved.\r\n");
		source.append(" */\r\n");
		source.append("\r\n");

		int index = 0;
		SourceUtils.insertLineComment(source, "", index++, true);

		String clazzName = interfaceClazz.getName();
		String simpleClazzName = clazzName;
		int idx = clazzName.lastIndexOf('.');
		if (idx != -1) {
			source.append("package ").append(simpleClazzName.substring(0, idx)).append(";\r\n");
			source.append("\r\n");
			
			simpleClazzName = clazzName.substring(idx + 1);
		}

		SourceUtils.insertLineComment(source, "", index++, true);

		Field[] clazzFields = interfaceClazz.getDeclaredFields();
		boolean hasImports = importAnnotationClasses(Arrays.asList(clazzFields), interfaceClazz, source);

		Class<?> superClazz = interfaceClazz.getSuperclass();
		if (superClazz != null) {
			String superClazzName = superClazz.getName();
			source.append("import ").append(superClazzName).append(";\r\n");
			source.append("\r\n");
			
			idx = superClazzName.lastIndexOf('.');
			if (idx != -1) {
				superClazzName = superClazzName.substring(idx + 1);
			}

			SourceUtils.insertLineComment(source, "", index++, true);
			
			source.append("public interface ").append(simpleClazzName).append(" extends ").append(superClazzName);
			SourceUtils.insertBlockComment(source, index++);
			source.append("{\r\n");
		} else {
			if (hasImports) {
				source.append("\r\n");
			}
			SourceUtils.insertLineComment(source, "", index++, true);
			
			source.append("public interface ").append(simpleClazzName);
			SourceUtils.insertBlockComment(source, index++);
			source.append("{\r\n");
		}
		source.append("\r\n");
		SourceUtils.insertLineComment(source, "\t", index++, true);

		boolean gotStaticFinalFields = false;
		
		for (int i = 0; i < clazzFields.length; i++) {
			Field f = clazzFields[i];
			int modifiers = f.getModifiers();
			if ((modifiers & (Modifier.PUBLIC/* | Modifier.PROTECTED*/)) != 0
					&& (modifiers & Modifier.STATIC) != 0 && (modifiers & Modifier.FINAL) != 0) {
				Class<?> type = f.getType();
				if (type == int.class || type == long.class || type == short.class 
						|| type == byte.class || type == char.class || type == double.class
						|| type == float.class || type == boolean.class || type == String.class) {
					generateAnnotation(f, !gotStaticFinalFields, source);
					source.append("\tpublic ").append(type.getSimpleName()).append(" ").append(f.getName()).append(" = ");
					try {
						if (type == int.class) {
							source.append(f.getInt(interfaceClazz));
						} else if (type == long.class) {
							source.append(f.getLong(interfaceClazz) + "L");
						} else if (type == short.class) {
							source.append(f.getShort(interfaceClazz));
						} else if (type == byte.class) {
							source.append(f.getByte(interfaceClazz));
						} else if (type == char.class) {
							source.append("\'").append(f.getChar(interfaceClazz)).append("\'");
						} else if (type == float.class) {
							source.append(f.getFloat(interfaceClazz));
						} else if (type == double.class) {
							source.append(f.getDouble(interfaceClazz));
						} else if (type == boolean.class) {
							if (f.getBoolean(interfaceClazz)) {
								source.append("true");
							} else {
								source.append("false");
							}
						} else if (type == String.class) {
							source.append("\"").append(f.get(interfaceClazz)).append("\"");
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
					source.append(";\r\n");
					gotStaticFinalFields = true;
				} else {
					System.out.println("Not supporting type " + type + " for field " + f.getName());
				}
			}
		}
		
		if (gotStaticFinalFields) {
			source.append("\r\n");
			SourceUtils.insertLineComment(source, "\t", index++, true);
		}

		source.append("}\r\n");
		return source.toString();
	}

	@SuppressWarnings("deprecation")
	public static String generateSourceFromObject(SimpleSerializable s) {
		StringBuilder source = new StringBuilder();
		Date date = new Date();
		source.append("/**\r\n");
		source.append(" * Generated by Java2Script.\r\n");
		source.append(" * Copyright (c) ").append(date.getYear() + 1900).append(" ").append(company).append(". All rights reserved.\r\n");
		source.append(" */\r\n");
		source.append("\r\n");
		
		int index = 0;
		SourceUtils.insertLineComment(source, "", index++, true);

		Class<?> clazz = s.getClass();
		String clazzName = clazz.getName();
		String simpleClazzName = clazzName;
		int idx = clazzName.lastIndexOf('.');
		if (idx != -1) {
			source.append("package ").append(simpleClazzName.substring(0, idx)).append(";\r\n");
			source.append("\r\n");
			
			simpleClazzName = clazzName.substring(idx + 1);
		}

		SourceUtils.insertLineComment(source, "", index++, true);
		
		boolean hasMoreImports = false;
		Set<String> importedClasses = new HashSet<String>();
		
		s.setSimpleVersion(SimpleSerializable.LATEST_SIMPLE_VERSION);
		Map<String, String> fieldMappings = s.fieldNameMapping();
		if (fieldMappings != null && fieldMappings.size() > 0) {
			hasMoreImports = true;
			String mapTypeName = "java.util.Map";
			source.append("import ").append(mapTypeName).append(";\r\n");
			importedClasses.add(mapTypeName);
		}

		Type[] interfaces = s.getClass().getGenericInterfaces();
		List<Type> interfaceList = new ArrayList<Type>();
		if (interfaces != null && interfaces.length > 0) {
			for (int i = 0; i < interfaces.length; i++) {
				Class<?> t = (Class<?>) interfaces[i];
				if (!SimpleSerializable.isSubInterfaceOf(t, ISimpleConstant.class)) {
					continue;
				}
				interfaceList.add(t);
				String typeName = t.getName();
				hasMoreImports = true;
				if (!importedClasses.contains(typeName)) {
					String simpleTypeName = typeName;
					source.append("import ").append(simpleTypeName).append(";\r\n");
					importedClasses.add(typeName);
				}
			}
		}


		boolean gotStaticFinalFields = false;
		Field[] clazzFields = clazz.getDeclaredFields();
		
		List<Field> fields = new ArrayList<Field>();
		List<Field> fields4Annotation = new ArrayList<Field>();
		Set<String> j2sIgnoredFileds = new HashSet<String>();
		
		for (int i = 0; i < clazzFields.length; i++) {
			Field f = clazzFields[i];
			int modifiers = f.getModifiers();
			if ((modifiers & (Modifier.PUBLIC/* | Modifier.PROTECTED*/)) != 0
					&& (modifiers & (Modifier.TRANSIENT | Modifier.STATIC)) == 0) {
				fields.add(f);
				if ((modifiers & Modifier.PROTECTED) != 0) {
					j2sIgnoredFileds.add(f.getName());
				}
			}
			if ((modifiers & (Modifier.PUBLIC/* | Modifier.PROTECTED*/)) != 0
					&& (modifiers & (Modifier.TRANSIENT/* | Modifier.STATIC*/)) == 0) {
				fields4Annotation.add(f);
			}
		}

		if (importAnnotationClasses(fields4Annotation, clazz, source)) {
			hasMoreImports = true;
		}
		
		for (Iterator<Field> itr = fields.iterator(); itr.hasNext();) {
			Field field = (Field) itr.next();
			Class<?> type = field.getType();
			
			if (SimpleSerializable.isSubclassOf(type, SimpleSerializable.class)
					|| SimpleSerializable.isSubclassOf(type, SimpleSerializable[].class)) {
				hasMoreImports = true;
				String typeName = type.isArray() ? type.getComponentType().getName() : type.getName();
				if (!importedClasses.contains(typeName)) {
					source.append("import ").append(typeName).append(";\r\n");
					importedClasses.add(typeName);
				}
			}
		}

		String[] fieldMapping = s.fieldMapping();
		Class<?> superClazz = s.getClass().getSuperclass();
		if (superClazz != null) {
			String superClazzName = superClazz.getName();
			source.append("import ").append(superClazzName).append(";\r\n");
			if (fieldMapping != null && fieldMappings.size() > 0 && System.getProperty("j2s.supports.web") != null) {
				source.append("import net.sf.j2s.annotation.J2SIgnore;\r\n");
			}
			if (SimpleSerializable.isSubclassOf(superClazz, SimplePipeRunnable.class)) {
				/*
				Method[] methods = s.getClass().getMethods();
				if (methods != null) {
					for (int i = 0; i < methods.length; i++) {
						Method m = methods[i];
						if ("deal".equals(m.getName())) {
							Class<?>[] params = m.getParameterTypes();
							if (params != null && params.length == 1) {
								Class<?> paramType = params[0];
								String paramClazzName = paramType.getName();
								source.append("import ").append(paramClazzName).append(";\r\n");
							}
						}
					}
				}
				// */
				Class<?>[] evtClazzes = eventClasses.toArray(new Class<?>[eventClasses.size()]);
				Arrays.sort(evtClazzes, new Comparator<Class<?>>() {

					public int compare(Class<?> c1, Class<?> c2) {
						String name1 = c1.getName();
						String name2 = c2.getName();
						return name1.compareTo(name2);
					}
					
				});
				source.append("import net.sf.j2s.ajax.SimpleSerializable;\r\n");
				for (int i = 0; i < evtClazzes.length; i++) {
					Class<?> evtClazz = evtClazzes[i];
					String evtClazzName = evtClazz.getName();
					if ("net.sf.j2s.ajax.SimpleSerializable".equals(evtClazzName)) {
						continue;
					}
					source.append("import ").append(evtClazzName).append(";\r\n");
				}
			}

			idx = superClazzName.lastIndexOf('.');
			if (idx != -1) {
				superClazzName = superClazzName.substring(idx + 1);
			}
			
			source.append("\r\n");
			SourceUtils.insertLineComment(source, "", index++, true);
			
			generateAnnotation(clazz, source);
			source.append("public class ").append(simpleClazzName).append(" extends ").append(superClazzName);
		} else {
			if (fieldMapping != null && fieldMappings.size() > 0 && System.getProperty("j2s.supports.web") != null) {
				source.append("import net.sf.j2s.annotation.J2SIgnore;\r\n");
				hasMoreImports = true;
			}
			if (hasMoreImports) {
				source.append("\r\n");
			}
			SourceUtils.insertLineComment(source, "", index++, true);

			generateAnnotation(clazz, source);
			source.append("public class ").append(simpleClazzName);
		}

		if (interfaceList.size() > 0) {
			boolean keywordAppended = false;
			for (int i = 0; i < interfaceList.size(); i++) {
				Class<?> t = (Class<?>) (Type) interfaceList.get(i);
				if (!SimpleSerializable.isSubInterfaceOf(t, ISimpleConstant.class)) {
					continue;
				}
				if (!keywordAppended) {
					source.append(" implements ");
					keywordAppended = true;
				}
				String typeName = t.getName();
				String simpleTypeName = typeName;
				idx = simpleTypeName.lastIndexOf('.');
				if (idx != -1) {
					simpleTypeName = simpleTypeName.substring(idx + 1);
				}
				
				source.append(simpleTypeName);
				if (i != interfaceList.size() -1) {
					source.append(", ");
				}
			}
		}

		SourceUtils.insertBlockComment(source, index++);
		source.append("{\r\n\r\n");
		SourceUtils.insertLineComment(source, "\t", index++, true);
		
		if (fieldMapping != null) {
			Map<String, Field> allFields = SimpleSerializable.getSerializableFields(clazzName, clazz);
			for (Iterator<String> itr = allFields.keySet().iterator(); itr.hasNext();) {
				String name = itr.next();
				boolean existed = false;
				for (int i = 0; i < fieldMapping.length / 2; i++) {
					String fName = fieldMapping[i + i];
					//String sName = fieldMapping[i + i + 1];
					if (fName.equals(name)) {
						existed = true;
						break;
					}
				}
				if (!existed) {
					System.err.println("[ERROR] Class " + clazzName + " field mappings does not contains field " + name);
					break;
				}
			}
			Set<String> names = new HashSet<String>();
			for (int i = 0; i < fieldMapping.length / 2; i++) {
				String fName = fieldMapping[i + i];
				String sName = fieldMapping[i + i + 1];
				if (names.contains(sName)) {
					System.err.println("[ERROR] Class " + clazzName + " field mappings shorten name " + sName + " duplicatedd.");
				}
				names.add(sName);
				boolean existed = false;
				for (Iterator<String> itr = allFields.keySet().iterator(); itr.hasNext();) {
					String name = itr.next();
					if (fName.equals(name)) {
						existed = true;
						break;
					}
				}
				if (!existed) {
					System.err.println("[ERROR] Class " + clazzName + " field mappings contains non-field " + fName);
					break;
				}
			}
		}
		if (fieldMappings != null && fieldMappings.size() > 0) {
			if (System.getProperty("j2s.supports.web") != null) {
				source.append("\t@J2SIgnore\r\n");
			}
			source.append("\tprivate static String[] mappings = new String[] {\r\n");
			for (Iterator<String> itr = fieldMappings.keySet().iterator(); itr.hasNext();) {
				String key = (String) itr.next();
				String value = fieldMappings.get(key);
				source.append("\t\t\t\"").append(key).append("\", \"").append(value).append("\",\r\n");
			}
			source.append("\t};\r\n");
			
			if (System.getProperty("j2s.supports.web") != null) {
				source.append("\t@J2SIgnore\r\n");
			}
			source.append("\tprivate static Map<String, String> nameMappings = mappingFromArray(mappings, false);\r\n");
			if (System.getProperty("j2s.supports.web") != null) {
				source.append("\t@J2SIgnore\r\n");
			}
			source.append("\tprivate static Map<String, String> aliasMappings = mappingFromArray(mappings, true);\r\n");
		}
		
		for (int i = 0; i < clazzFields.length; i++) {
			Field f = clazzFields[i];
			String name = f.getName();
			if (j2sIgnoredFileds.contains(name)) {
				System.out.println("Ignoring ..." + name);
				continue;
			}
			int modifiers = f.getModifiers();
			if ((modifiers & (Modifier.PUBLIC/* | Modifier.PROTECTED*/)) != 0
					&& (modifiers & Modifier.STATIC) != 0 && (modifiers & Modifier.FINAL) != 0) {
				Class<?> type = f.getType();
				if (type == int.class || type == long.class || type == short.class 
						|| type == byte.class || type == char.class || type == double.class
						|| type == float.class || type == boolean.class || type == String.class) {
					if (!gotStaticFinalFields && fieldMappings != null && fieldMappings.size() > 0) {
						source.append("\r\n");
					}
					generateAnnotation(f, !gotStaticFinalFields, source);
					source.append("\tpublic static final ").append(type.getSimpleName()).append(" ").append(f.getName()).append(" = ");
					try {
						if (type == int.class) {
							source.append("" + f.getInt(s.getClass()));
						} else if (type == long.class) {
							source.append(f.getLong(s.getClass()) + "L");
						} else if (type == short.class) {
							source.append("" + f.getShort(s.getClass()));
						} else if (type == byte.class) {
							source.append("" + f.getByte(s.getClass()));
						} else if (type == char.class) {
							source.append("\'" + f.getChar(s.getClass()) + "\'");
						} else if (type == float.class) {
							source.append("" + f.getFloat(s.getClass()));
						} else if (type == double.class) {
							source.append("" + f.getDouble(s.getClass()));
						} else if (type == boolean.class) {
							if (f.getBoolean(s.getClass())) {
								source.append("true");
							} else {
								source.append("false");
							}
						} else if (type == String.class) {
							source.append("\"" + f.get(s.getClass()) + "\"");
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
					source.append(";\r\n");
					gotStaticFinalFields = true;
				} else {
					System.out.println("Not supporting type " + type + " for field " + f.getName());
				}
			}
		}
		
		source.append("\r\n");
		SourceUtils.insertLineComment(source, "\t", index++, true);

		boolean firstField = true;
		for (Iterator<Field> itr = fields.iterator(); itr.hasNext();) {
			Field field = (Field) itr.next();
			String name = field.getName();
			Class<?> type = field.getType();
			
			if (type == int.class || type == long.class || type == short.class 
					|| type == byte.class || type == char.class || type == double.class
					|| type == float.class || type == boolean.class || type == String.class
					|| SimpleSerializable.isSubclassOf(type, SimpleSerializable.class)
					|| type == int[].class || type == long[].class || type == double[].class
					|| type == byte[].class || type == short[].class || type == char[].class
					|| type == float[].class || type == boolean[].class || type == String[].class
					|| SimpleSerializable.isSubclassOf(type, SimpleSerializable[].class)) {
				generateAnnotation(field, firstField, source);
				firstField = false;
				source.append("\tpublic ").append(type.getSimpleName()).append(" ").append(name).append(";\r\n");
			} else {
				System.out.println("Unsupported type " + type);
			}
		}
		
		if (!firstField) { // contains fields
			source.append("\r\n");
		} // else no blank line as previous comment block contains a blank line
		SourceUtils.insertLineComment(source, "\t", index++, true);
		boolean moreCodesAdded = false;
		if (fieldMappings != null && fieldMappings.size() > 0) {
			if (System.getProperty("j2s.supports.web") != null) {
				source.append("\t@J2SIgnore\r\n");
			}
			source.append("\t@Override\r\n");
			source.append("\tprotected Map<String, String> fieldNameMapping() {\r\n");
			source.append("\t\treturn nameMappings;\r\n");
			source.append("\t}\r\n\r\n");
			if (System.getProperty("j2s.supports.web") != null) {
				source.append("\t@J2SIgnore\r\n");
			}
			source.append("\t@Override\r\n");
			source.append("\tprotected Map<String, String> fieldAliasMapping() {\r\n");
			source.append("\t\treturn aliasMappings;\r\n");
			source.append("\t}\r\n\r\n");
			moreCodesAdded = true;
		}
		if (s.bytesCompactMode()) {
			source.append("\tpublic boolean bytesCompactMode() {\r\n");
			source.append("\t\treturn true;\r\n");
			source.append("\t}\r\n");
			source.append("\r\n");
			moreCodesAdded = true;
		}
		if (s instanceof SimplePipeRunnable) {
			source.append("\t@Override\r\n");
			source.append("\tpublic boolean pipeSetup() {\r\n");
			source.append("\t\treturn true;\r\n");
			source.append("\t}\r\n");
			source.append("\r\n");
			source.append("\t@Override\r\n");
			source.append("\tpublic SimpleSerializable[] through(Object... args) {\r\n");
			source.append("\t\treturn null;\r\n");
			source.append("\t}\r\n");
			source.append("\r\n");
			
			/*
			Method[] methods = s.getClass().getMethods();
			if (methods != null) {
				for (int i = 0; i < methods.length; i++) {
					Method m = methods[i];
					if ("deal".equals(m.getName())) {
						Class<?>[] params = m.getParameterTypes();
						if (params != null && params.length == 1) {
							Class<?> paramType = params[0];
							String paramClazzName = paramType.getName();
							if ("net.sf.j2s.ajax.SimpleSerializable".equals(paramClazzName)) {
								continue;
							}
							int paramIdx = paramClazzName.lastIndexOf('.');
							if (paramIdx != -1) {
								paramClazzName = paramClazzName.substring(paramIdx + 1);
							}
							source.append("\tpublic boolean deal(");
							source.append(paramClazzName);
							source.append(" e) {\r\n");
							source.append("\t\treturn true;\r\n");
							source.append("\t}\r\n");
							source.append("\r\n");
						}
					}
				}
			}
			// */
			Class<?>[] evtClazzes = eventClasses.toArray(new Class<?>[eventClasses.size()]);
			Arrays.sort(evtClazzes, new Comparator<Class<?>>() {

				public int compare(Class<?> c1, Class<?> c2) {
					String name1 = c1.getSimpleName();
					String name2 = c2.getSimpleName();
					return name1.compareTo(name2);
				}
				
			});
			for (int i = 0; i < evtClazzes.length; i++) {
				Class<?> evtClazz = evtClazzes[i];
				String evtClazzName = evtClazz.getName();
				if ("net.sf.j2s.ajax.SimpleSerializable".equals(evtClazzName)) {
					continue;
				}
				int paramIdx = evtClazzName.lastIndexOf('.');
				if (paramIdx != -1) {
					evtClazzName = evtClazzName.substring(paramIdx + 1);
				}
				source.append("\tpublic boolean deal(").append(evtClazzName).append(" e) {\r\n");
				source.append("\t\treturn true;\r\n");
				source.append("\t}\r\n");
				source.append("\r\n");
			}
			moreCodesAdded = true;
		} else if (s instanceof SimpleRPCRunnable) {
			source.append("\t@Override\r\n");
			source.append("\tpublic void ajaxRun() {\r\n");
			source.append("\t}\r\n");
			source.append("\r\n");
			moreCodesAdded = true;
		}
		if (moreCodesAdded) {
			SourceUtils.insertLineComment(source, "\t", index++, true);
		}
		source.append("}\r\n");

		return source.toString();
	}

	private static void generateAnnotation(Class<?> clazz, StringBuilder source) {
		Deprecated annDeprecated = clazz.getAnnotation(Deprecated.class);
		if (annDeprecated != null) {
			source.append("@").append(annDeprecated.annotationType().getSimpleName()).append("\r\n");
		}
		SimpleComment annComment = clazz.getAnnotation(SimpleComment.class);
		if (annComment != null) {
			source.append("@").append(annComment.annotationType().getSimpleName());
			String[] comments = annComment.value();
			if (comments != null && comments.length == 1) {
				source.append("(\"");
				source.append(wrapString(comments[0]));
				source.append("\")");
			} else if (comments != null && comments.length >= 2) {
				source.append("({\r\n");
				for (int i = 0; i < comments.length; i++) {
					source.append("\t\"");
					source.append(wrapString(comments[i]));
					source.append(i == comments.length - 1 ? "\"\r\n" : "\",\r\n");
				}
				source.append("})");
			}
			source.append("\r\n");
		}
	}

	private static boolean importAnnotationClasses(List<Field> fields,
			Class<?> clazz, StringBuilder source) {
		boolean inAnnImported = false;
		boolean outAnnImported = false;
		boolean inOutAnnImported = false;
		boolean commentAnnImported = false;
		if (clazz.getAnnotation(SimpleComment.class) != null) {
			commentAnnImported = true;
		}
		for (Iterator<Field> itr = fields.iterator(); itr.hasNext();) {
			Field field = (Field) itr.next();
			if (!inAnnImported) {
				if (field.getAnnotation(SimpleIn.class) != null) {
					inAnnImported = true;
					continue;
				}
			}
			if (!outAnnImported) {
				if (field.getAnnotation(SimpleOut.class) != null) {
					outAnnImported = true;
					continue;
				}
			}
			if (!inOutAnnImported) {
				if (field.getAnnotation(SimpleInOut.class) != null) {
					inOutAnnImported = true;
					continue;
				}
			}
			if (!commentAnnImported) {
				if (field.getAnnotation(SimpleComment.class) != null) {
					commentAnnImported = true;
					continue;
				}
			}
			if (inAnnImported && outAnnImported && inOutAnnImported && commentAnnImported) {
				break;
			}
		}
		if (commentAnnImported) {
			source.append("import ").append(SimpleComment.class.getName()).append(";\r\n");
		}
		if (inAnnImported) {
			source.append("import ").append(SimpleIn.class.getName()).append(";\r\n");
		}
		if (inOutAnnImported) {
			source.append("import ").append(SimpleOut.class.getName()).append(";\r\n");
		}
		if (outAnnImported) {
			source.append("import ").append(SimpleOut.class.getName()).append(";\r\n");
		}
		return inAnnImported || outAnnImported || inOutAnnImported || commentAnnImported;
	}

	private static String wrapString(String s) {
		if (s == null) {
			return "";
		}
		return s.replaceAll("\\\\", "\\\\\\\\")
				.replaceAll("\r", "\\\\r")
				.replaceAll("\n", "\\\\n")
				.replaceAll("\t", "\\\\t")
				.replaceAll("\"", "\\\\\"");
	}
	
	private static boolean generateAnnotation(Field field, boolean firstField, StringBuilder source) {
		Deprecated annDeprecated = field.getAnnotation(Deprecated.class);
		if (annDeprecated != null) {
			source.append(firstField ? "\t@" : "\r\n\t@");
			source.append(annDeprecated.annotationType().getSimpleName());
		}
		SimpleIn annIn = field.getAnnotation(SimpleIn.class);
		if (annIn != null) {
			source.append(firstField && annDeprecated == null ? "\t@" : "\r\n\t@");
			source.append(annIn.annotationType().getSimpleName());
			String[] comments = annIn.value();
			if (comments != null && comments.length == 1
					&& comments[0] != null && comments[0].length() > 0) {
				source.append("(\"").append(wrapString(comments[0])).append("\")");
			} else if (comments != null && comments.length >= 2) {
				source.append("({\r\n");
				for (int i = 0; i < comments.length; i++) {
					source.append("\t\t\"").append(wrapString(comments[i])).append(i == comments.length - 1 ? "\"\r\n" : "\",\r\n");
				}
				source.append("\t})");
			}
			source.append("\r\n");
			return true;
		}
		SimpleOut annOut = field.getAnnotation(SimpleOut.class);
		if (annOut != null) {
			source.append(firstField && annDeprecated == null ? "\t@" : "\r\n\t@");
			source.append(annOut.annotationType().getSimpleName());
			String[] comments = annOut.value();
			if (comments != null && comments.length == 1
					&& comments[0] != null && comments[0].length() > 0) {
				source.append("(\"").append(wrapString(comments[0])).append("\")");
			} else if (comments != null && comments.length >= 2) {
				source.append("({\r\n");
				for (int i = 0; i < comments.length; i++) {
					source.append("\t\t\"").append(wrapString(comments[i])).append(i == comments.length - 1 ? "\"\r\n" : "\",\r\n");
				}
				source.append("\t})");
			}
			source.append("\r\n");
			return true;
		}
		SimpleInOut annInOut = field.getAnnotation(SimpleInOut.class);
		if (annInOut != null) {
			source.append(firstField && annDeprecated == null ? "\t@" : "\r\n\t@");
			source.append(annInOut.annotationType().getSimpleName());
			String[] comments = annInOut.value();
			if (comments != null && comments.length == 1
					&& comments[0] != null && comments[0].length() > 0) {
				source.append("(\"").append(wrapString(comments[0])).append("\")");
			} else if (comments != null && comments.length >= 2) {
				source.append("({\r\n");
				for (int i = 0; i < comments.length; i++) {
					source.append("\t\t\"").append(wrapString(comments[i])).append(i == comments.length - 1 ? "\"\r\n" : "\",\r\n");
				}
				source.append("\t})");
			}
			source.append("\r\n");
			return true;
		}
		SimpleComment annComment = field.getAnnotation(SimpleComment.class);
		if (annComment != null) {
			source.append(firstField && annDeprecated == null ? "\t@" : "\r\n\t@");
			source.append(annComment.annotationType().getSimpleName());
			String[] comments = annComment.value();
			if (comments != null && comments.length == 1) {
				source.append("(\"").append(wrapString(comments[0])).append("\")");
			} else if (comments != null && comments.length >= 2) {
				source.append("({\r\n");
				for (int i = 0; i < comments.length; i++) {
					source.append("\t\t\"").append(wrapString(comments[i])).append(i == comments.length - 1 ? "\"\r\n" : "\",\r\n");
				}
				source.append("\t})");
			}
			source.append("\r\n");
			return true;
		}
		if (annDeprecated != null) {
			source.append("\r\n");
			return true;
		}
		return false;
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		if (args == null || args.length < 4) {
			System.out.println("Usage: " + SimpleSource4Java.class.getName() + " <sources folder> <author> <orgization or company> <mapping> <class> [class ...]");
			return;
		}
		String targetFolder = args[0];
		File f = new File(targetFolder);
		if (f.exists()) {
			if (!f.isDirectory()) {
				System.out.println("Target folder " + f.getAbsolutePath() + " is not a folder.");
				return;
			}
		} else {
			boolean ok = f.mkdirs();
			if (!ok) {
				System.out.println("Failed to create target folder " + f.getAbsolutePath() + ".");
				return;
			}
		}
		folder = f.getName();
		author = args[1];
		company = args[2];
		String mappingClass = args[3];

		for (int i = 1 + 2 + 1; i < args.length; i++) {
			String j2sSimpleClazz = args[i];
			try {
				Class<?> clazz = Class.forName(j2sSimpleClazz);
				if (clazz.isInterface()) {
					continue;
				}
				Object inst = clazz.newInstance();
				if (inst instanceof SimpleSerializable && !(inst instanceof SimpleRPCRunnable)) {
					eventClasses.add(clazz);
				}
			} catch (Throwable e) {
				System.out.println("Error: " + j2sSimpleClazz);
				e.printStackTrace();
			}
		}

		for (int i = 1 + 2 + 1; i < args.length; i++) {
			String j2sSimpleClazz = args[i];
			try {
				Class<?> clazz = Class.forName(j2sSimpleClazz);
				if (clazz.isInterface()) {
					String simpleName = j2sSimpleClazz;
					String packageName = null;
					int idx = j2sSimpleClazz.lastIndexOf('.');
					if (idx != -1) {
						packageName = j2sSimpleClazz.substring(0, idx);
						packageName = packageName.replace('.', File.separatorChar);
						simpleName = j2sSimpleClazz.substring(idx + 1);
					}
					String javaSource = generateSourceFromInterface(clazz);
					String targetPath = targetFolder;
					if (packageName != null) {
						if (targetPath.endsWith(File.separator)) {
							targetPath = targetPath + packageName;
						} else {
							targetPath = targetPath + File.separator + packageName;
						}
						File folder = new File(targetPath);
						if (!folder.exists()) {
							folder.mkdirs();
						}
					}
					File javaFile = new File(targetPath, simpleName + ".java");
					SourceUtils.updateSourceContent(javaFile, javaSource);
					System.out.println(javaFile.getAbsolutePath());
					continue;
				}
				Object inst = clazz.newInstance();
				if (inst instanceof SimpleSerializable) {
					SimpleSerializable s = (SimpleSerializable) inst;
					
					String simpleName = j2sSimpleClazz;
					String packageName = null;
					int idx = j2sSimpleClazz.lastIndexOf('.');
					if (idx != -1) {
						packageName = j2sSimpleClazz.substring(0, idx);
						packageName = packageName.replace('.', File.separatorChar);
						simpleName = j2sSimpleClazz.substring(idx + 1);
					}
					String javaSource = generateSourceFromObject(s);
					String targetPath = targetFolder;
					if (packageName != null) {
						if (targetPath.endsWith(File.separator)) {
							targetPath = targetPath + packageName;
						} else {
							targetPath = targetPath + File.separator + packageName;
						}
						File folder = new File(targetPath);
						if (!folder.exists()) {
							folder.mkdirs();
						}
					}
					File javaFile = new File(targetPath, simpleName + ".java");
					SourceUtils.updateSourceContent(javaFile, javaSource); 
					System.out.println(javaFile.getAbsolutePath());
				}
			} catch (Throwable e) {
				System.out.println("Error: " + j2sSimpleClazz);
				e.printStackTrace();
			}
		}
		
		{
			String clazzName = mappingClass;
			StringBuilder source = new StringBuilder();
			Date date = new Date();
			source.append("/**\r\n");
			source.append(" * Generated by Java2Script.\r\n");
			source.append(" * Copyright (c) ");
			source.append(date.getYear() + 1900);
			source.append(" ");
			source.append(company);
			source.append(". All rights reserved.\r\n");
			source.append(" */\r\n");
			source.append("\r\n");

			int index = 0;
			SourceUtils.insertLineComment(source, "", index++, true);

			String simpleClazzName = clazzName;
			int idx = clazzName.lastIndexOf('.');
			if (idx != -1) {
				source.append("package ").append(simpleClazzName.substring(0, idx)).append(";\r\n");
				source.append("\r\n");
				
				simpleClazzName = clazzName.substring(idx + 1);
			}

			SourceUtils.insertLineComment(source, "", index++, true);
			source.append("import net.sf.j2s.ajax.SimpleSerializable;\r\n");
			source.append("\r\n");
			
			SourceUtils.insertLineComment(source, "", index++, true);
			source.append("public class ").append(simpleClazzName);
			SourceUtils.insertBlockComment(source, index++);
			source.append("{\r\n");
			source.append("\r\n");
			SourceUtils.insertLineComment(source, "\t", index++, true);

			source.append("\tpublic static void initializeMappings() {\r\n");
			SourceUtils.insertLineComment(source, "\t\t", index++, false);
			for (int i = 1 + 2 + 1; i < args.length; i++) {
				String j2sSimpleClazz = args[i];
				try {
					Class<?> clazz = Class.forName(j2sSimpleClazz);
					if (clazz.isInterface()) {
						continue;
					}
					Object inst = clazz.newInstance();
					if (inst instanceof SimpleSerializable) {
						String shortenName = SimpleSerializable.getClassShortenName(j2sSimpleClazz);
						if (shortenName != null && shortenName.length() > 0) {
							source.append("\t\tSimpleSerializable.registerClassShortenName(\"").append(j2sSimpleClazz).append("\", \"").append(shortenName).append("\");\r\n");
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			SourceUtils.insertLineComment(source, "\t\t", index++, false);
			source.append("\t}\r\n");
			
			source.append("\r\n");
			SourceUtils.insertLineComment(source, "\t", index++, true);

			source.append("}\r\n");
			
			String simpleName = mappingClass;
			String packageName = null;
			idx = mappingClass.lastIndexOf('.');
			if (idx != -1) {
				packageName = mappingClass.substring(0, idx);
				packageName = packageName.replace('.', File.separatorChar);
				simpleName = mappingClass.substring(idx + 1);
			}
			if (packageName != null) {
				if (targetFolder.endsWith(File.separator)) {
					targetFolder = targetFolder + packageName;
				} else {
					targetFolder = targetFolder + File.separator + packageName;
				}
				File folder = new File(targetFolder);
				if (!folder.exists()) {
					folder.mkdirs();
				}
			}
			File javaFile = new File(targetFolder, simpleName + ".java");
			SourceUtils.updateSourceContent(javaFile, source.toString());
		}

	}

}
