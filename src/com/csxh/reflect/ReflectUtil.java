package com.csxh.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.csxh.jdbc.JdbcUtil;
import com.csxh.string.StringUtil;

/*
 * 反射工具类，为ORM提供支持
 */
public class ReflectUtil {

	// 配置日志对象
	private static Logger log = Logger.getLogger(JdbcUtil.class);

	// 提供实体类对应的表名称
	public static String getTableName(Class<?> clazz) {

		String table =StringUtil.toLowerCaseAt(clazz.getSimpleName());
		return table;
	}

	// 提供实体类对应的字段名称列表
	public static List<String> getTableFieldList(Class<?> clazz) {

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

				String field =StringUtil.toLowerCaseAt(_Xxx);

				fieldList.add(field);
				// 获取返回值的类型信息
				fieldTypeList.add(method.getReturnType());

			}
		}

		return fieldList;

	}

	public static List<String> getJavaBeanFieldList(Class<?> clazz) {
		return ReflectUtil.getTableFieldList(clazz);
	}
	
	// 提供对应实体对象（符合JavaBean规范）属性的类型
	public static Class<?> getJavaBeanFieldType(Class<?> clazz, String fieldName) {
		
		Method m = null;
		
		String getXxx = "get" + StringUtil.toUpperCaseAt(fieldName);
		
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
		
		fieldName=StringUtil.toUpperCaseAt(fieldName);
		
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
	
	// 提供对应属性的get方法
	public static Method getJavaBeanGetMethod(Class<?> clazz, String fieldName) {
		
		Method m = null;
		
		fieldName=StringUtil.toUpperCaseAt(fieldName);
		
		String getXxx = "get" + fieldName;
		try {
			m = clazz.getMethod(getXxx);
			return m;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return m;
	}
	
	public static void setJavaBeanValue(Object javaBean,String name,Object value){
		Method m=ReflectUtil.getJavaBeanSetMethod(javaBean.getClass(), name);
		if(m!=null){
			try {
				m.invoke(javaBean, value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
	}

	public static Object getJavaBeanValue(Object javaBean,String name){
		
		Method m=ReflectUtil.getJavaBeanGetMethod(javaBean.getClass(), name);
		
		if(m!=null){
			try {
				return m.invoke(javaBean);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		
		return null;
		
	}

}
