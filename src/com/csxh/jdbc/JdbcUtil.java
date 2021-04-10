package com.csxh.jdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/*
 * Jdbc����������
 */
public class JdbcUtil {

	//������־����
	private static Logger log=Logger.getLogger(JdbcUtil.class);
	//����jdbc���Ӳ��������ӵ�url���û���user������password
	private static String url;
	private static String user;
	private static String password;
	
	static{
		try {
			//1��ʹ��PropertY���ȡjdbc�����ò���
			Properties p=new Properties();
			InputStream in=JdbcUtil.class.getClassLoader().getResourceAsStream("/jdbc.properties");
			p.load(in);
			JdbcUtil.url=p.getProperty("url");
			JdbcUtil.user=p.getProperty("user");
			JdbcUtil.password=p.getProperty("password");
			log.info("��ȡjdbc���ò������");
			//2��ʹ�÷��������jdbc��������
			Class.forName("com.mysql.jdbc.Driver");
			log.info("����jdbc������ɹ�");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		}
		
	}
	
	
	
}
