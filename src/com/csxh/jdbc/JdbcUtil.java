package com.csxh.jdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/*
 * Jdbc工具类的设计
 */
public class JdbcUtil {

	//配置日志对象
	private static Logger log=Logger.getLogger(JdbcUtil.class);
	//配置jdbc连接参数：连接的url，用户名user与密码password
	private static String url;
	private static String user;
	private static String password;
	
	static{
		try {
			//1、使用PropertY类读取jdbc的配置参数
			Properties p=new Properties();
			InputStream in=JdbcUtil.class.getClassLoader().getResourceAsStream("/jdbc.properties");
			p.load(in);
			JdbcUtil.url=p.getProperty("url");
			JdbcUtil.user=p.getProperty("user");
			JdbcUtil.password=p.getProperty("password");
			log.info("读取jdbc配置参数完成");
			//2、使用反射类加载jdbc的驱动类
			Class.forName("com.mysql.jdbc.Driver");
			log.info("加载jdbc驱动类成功");
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
