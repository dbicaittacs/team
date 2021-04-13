package com.csxh.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.csxh.jdbc.JdbcUtil;

/*
 * 反射工具类，为ORM提供支持
 */
public class ReflectUtil {

	// 配置日志对象
	private static Logger log = Logger.getLogger(JdbcUtil.class);

	// 提供实体类对应的表名称
	public static String getTableName(Class<?> clazz) {

		char[] chs = clazz.getSimpleName().toCharArray();
		chs[0] = Character.toLowerCase(chs[0]);
		String table = new String(chs);
		return table;
	}

	// 提供实体类对应的字段名称列表
	public static List<String> getJavaBeanFieldList(Class<?> clazz) {

		// 获取表的各个字段名称
		char[] chs;
		List<Method> setMethodList = new ArrayList<Method>();
		List<String> fieldList = new ArrayList<String>();
		List<Class<?>> fieldTypeList = new ArrayList<Class<?>>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {

			if (method.getModifiers() == Modifier.PUBLIC && method.getName().startsWith("get")) {
				// 提取getXxx中的Xxx部分
				String _Xxx = method.getName().substring(3);
				// 判断是否有对象setXxx方法
				String setXxx = "set" + _Xxx;

				try {
					Method m = clazz.getMethod(setXxx, method.getReturnType());
					if (m == null) {
						continue;// 忽略后面的代码，继续下一次循环
					}
					setMethodList.add(m);

				} catch (NoSuchMethodException e) {
					log.error(e.getMessage());
					continue;// 忽略后面的代码，继续下一次循环
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					log.error(e.getMessage());
					continue;// 忽略后面的代码，继续下一次循环
				}

				chs = _Xxx.toCharArray();

				chs[0] = Character.toLowerCase(chs[0]);
				String field = new String(chs);

				fieldList.add(field);
				// 获取返回值的类型信息
				fieldTypeList.add(method.getReturnType());

			}
		}

		return fieldList;

	}

	// 提供对应实体对象（符合JavaBean规范）属性的类型
	public static Class<?> getJavaBeanFieldType(Class<?> clazz, String fieldName) {
		
		Method m = null;
		
		char[] chs=fieldName.toCharArray();
		chs[0]=Character.toUpperCase(chs[0]);
		fieldName=new String(chs);
		
		String getXxx = "get" + fieldName;
		
		try {
			m = clazz.getMethod(getXxx);
			return m.getReturnType();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return null;
	}

	// 提供对应属性的set方法
	public static Method getJavaBeanSetMethod(Class<?> clazz, String fieldName) {

		Method m = null;
		
		char[] chs=fieldName.toCharArray();
		chs[0]=Character.toUpperCase(chs[0]);
		fieldName=new String(chs);
		
		String getXxx = "get" + fieldName;
		String setXxx = "set" + fieldName;
		try {
			m = clazz.getMethod(getXxx);
			// 判断是否有对象setXxx方法
			m = clazz.getMethod(setXxx, m.getReturnType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return m;
	}

}
