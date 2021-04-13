package com.csxh.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.csxh.jdbc.JdbcUtil;

/*
 * ���乤���࣬ΪORM�ṩ֧��
 */
public class ReflectUtil {

	// ������־����
	private static Logger log = Logger.getLogger(JdbcUtil.class);

	// �ṩʵ�����Ӧ�ı�����
	public static String getTableName(Class<?> clazz) {

		char[] chs = clazz.getSimpleName().toCharArray();
		chs[0] = Character.toLowerCase(chs[0]);
		String table = new String(chs);
		return table;
	}

	// �ṩʵ�����Ӧ���ֶ������б�
	public static List<String> getJavaBeanFieldList(Class<?> clazz) {

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

				chs = _Xxx.toCharArray();

				chs[0] = Character.toLowerCase(chs[0]);
				String field = new String(chs);

				fieldList.add(field);
				// ��ȡ����ֵ��������Ϣ
				fieldTypeList.add(method.getReturnType());

			}
		}

		return fieldList;

	}

	// �ṩ��Ӧʵ����󣨷���JavaBean�淶�����Ե�����
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

	// �ṩ��Ӧ���Ե�set����
	public static Method getJavaBeanSetMethod(Class<?> clazz, String fieldName) {

		Method m = null;
		
		char[] chs=fieldName.toCharArray();
		chs[0]=Character.toUpperCase(chs[0]);
		fieldName=new String(chs);
		
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

}
