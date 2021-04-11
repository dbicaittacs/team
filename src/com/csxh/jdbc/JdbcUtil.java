package com.csxh.jdbc;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/*
 * Jdbc工具类的设计
 */
public class JdbcUtil {

	// 配置日志对象
	private static Logger log = Logger.getLogger(JdbcUtil.class);
	// 配置jdbc连接参数：连接的url，用户名user与密码password
	private static String url;
	private static String user;
	private static String password;
	private static Connection conn;
	private static Statement stmt;

	static {
		try {
			// 1、使用PropertY类读取jdbc的配置参数
			Properties p = new Properties();
			InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
			// InputStream
			// in2=JdbcUtil.class.getResourceAsStream("/jdbc.properties");
			p.load(in);
			in.close();
			JdbcUtil.url = p.getProperty("url");
			JdbcUtil.user = p.getProperty("user");
			JdbcUtil.password = p.getProperty("password");
			log.info("读取jdbc配置参数完成");
			// 2、使用反射类加载jdbc的驱动类
			Class.forName("com.mysql.jdbc.Driver");
			JdbcUtil.conn = null;
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

	// 3、通过驱动管理类来创建并打开一个连接对象
	public static Connection openConnection() {

		try {

			if (JdbcUtil.conn == null) {
				JdbcUtil.conn = DriverManager.getConnection(JdbcUtil.url, JdbcUtil.user, JdbcUtil.password);
			}

		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return JdbcUtil.conn;
	}

	// 4、使用连接对象创建SQL语句对象,并向服务器程序发送SQL语句
	public static boolean excute(String sql) {

		boolean b = true;
		try {
			// 4.1打开连接对象
			JdbcUtil.conn = JdbcUtil.openConnection();
			JdbcUtil.stmt = conn.createStatement();
			log.info("创建语句对象成功");
			// 4.3通过语句对象执行SQL语句
			JdbcUtil.stmt.execute(sql);

			log.info("执行SQL语句");
			// 4.5返回执行的结果
			return b;

		} catch (SQLException e) {
			log.error(e.getMessage());
			b = false;
		} finally {
			// 4.4 关闭连接对象
			JdbcUtil.closeConnection();
		}

		return b;

	}

	// 执行查询语句 select --->返回结果集--->列表集合
	public static List<Object[]> findList(String selectSQL) {

		List<Object[]> retList = new ArrayList<Object[]>();
		try {
			JdbcUtil.conn = JdbcUtil.openConnection();
			JdbcUtil.stmt = JdbcUtil.conn.createStatement();
			ResultSet rs = JdbcUtil.stmt.executeQuery(selectSQL);
			log.info("获取查询的结果集");
			// 获取结果集的列数（字段数）
			int columnCount = rs.getMetaData().getColumnCount();
			// 对结果集进行迭代取值
			while (rs.next()) {
				// 创建Object数组，以便容纳各列的记录值
				Object[] os = new Object[columnCount];
				for (int column = 0; column < columnCount; column++) {
					os[column] = rs.getObject(column + 1);// 列的序列是从1开始的
				}
				retList.add(os);
			}
			log.info("将结果集中的记录转换成列表数组");

		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			JdbcUtil.closeConnection();
		}

		return retList;

	}

	// 关闭已经打开的连接对象
	public static void closeConnection() {

		// 首先关闭语句对象
		if (JdbcUtil.stmt != null) {
			try {
				JdbcUtil.stmt.close();
				JdbcUtil.stmt = null;// 已经关闭标志
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		// 再关闭连接对象
		if (JdbcUtil.conn != null) {
			try {
				JdbcUtil.conn.close();
				JdbcUtil.conn = null;// 清空，以便标志下一次需要创建新连接对象
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}


}
