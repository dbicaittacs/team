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
 * ���乤���࣬ΪORM�ṩ֧��
 */
public class ReflectUtil {

	// ������־����
	private static Logger log = Logger.getLogger(JdbcUtil.class);

	// �ṩʵ�����Ӧ�ı�����
	public static String getTableName(Class<?> clazz) {

		String table =StringUtil.toLowerCaseAt(clazz.getSimpleName());
		return table;
	}

	// �ṩʵ�����Ӧ���ֶ������б�
	public static List<String> getTableFieldList(Class<?> clazz) {

		// ��ȡ��ĸ����ֶ�����
		char[] chs;
		List<Method> setMethodList = new ArrayList<Method>();
		List<String> fieldList = new ArrayList<String>();
		List<Class<?>> fieldTypeList = new ArrayList<Class<?>>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {

			if (method.getModifiers() == Modifier.PUBLIC && method.getName().startsWith("get")) {
				// ��ȡgetXxx�е�Xxx����
				String _Xxx = method.getName().substring(3);
				// �ж��Ƿ��ж���setXxx����
				String setXxx = "set" + _Xxx;

				try {
					Method m = clazz.getMethod(setXxx, method.getReturnType());
					if (m == null) {
						continue;// ���Ժ���Ĵ��룬������һ��ѭ��
					}
					setMethodList.add(m);

				} catch (NoSuchMethodException e) {
					log.error(e.getMessage());
					continue;// ���Ժ���Ĵ��룬������һ��ѭ��
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					log.error(e.getMessage());
					continue;// ���Ժ���Ĵ��룬������һ��ѭ��
				}

				String field =StringUtil.toLowerCaseAt(_Xxx);

				fieldList.add(field);
				// ��ȡ����ֵ��������Ϣ
				fieldTypeList.add(method.getReturnType());

			}
		}

		return fieldList;

	}

	public static List<String> getJavaBeanFieldList(Class<?> clazz) {
		return ReflectUtil.getTableFieldList(clazz);
	}
	
	// �ṩ��Ӧʵ����󣨷���JavaBean�淶�����Ե�����
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

	// �ṩ��Ӧ���Ե�set����
	public static Method getJavaBeanSetMethod(Class<?> clazz, String fieldName) {

		Method m = null;
		
		fieldName=StringUtil.toUpperCaseAt(fieldName);
		
		String getXxx = "get" + fieldName;
		String setXxx = "set" + fieldName;
		try {
			m = clazz.getMethod(getXxx);
			// �ж��Ƿ��ж���setXxx����
			m = clazz.getMethod(setXxx, m.getReturnType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return m;
	}
	
	// �ṩ��Ӧ���Ե�get����
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
