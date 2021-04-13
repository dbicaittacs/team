package com.csxh.jdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.csxh.reflect.ReflectUtil;

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
	private static PreparedStatement pstmt;

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

	// 对数据库记录进行删除
	public static boolean delete(String deleteSQL,Object...args) {

		boolean fOk = false;
		try {
			JdbcUtil.conn = JdbcUtil.openConnection();

			// 取消事务的自动提交事务功能
			JdbcUtil.conn.setAutoCommit(false);

			JdbcUtil.pstmt = JdbcUtil.conn.prepareStatement(deleteSQL);
			for (int i = 0; i < args.length; i++) {
				JdbcUtil.pstmt.setObject(i + 1, args[i]);
			}
			// JdbcUtil.stmt = JdbcUtil.conn.createStatement();

			// int ret=JdbcUtil.stmt.executeUpdate(updateSQL);
			int ret = JdbcUtil.pstmt.executeUpdate();

			// JdbcUtil.stmt = JdbcUtil.conn.createStatement();

			// int ret=JdbcUtil.stmt.executeUpdate(deleteSQL);
			// 手动提交事务
			JdbcUtil.conn.commit();

			log.info("删除记录成功");

			fOk = ret > 0;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtil.conn.rollback();
				log.info("删除没有成功，回滚到删除之前的状态");
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
			log.error(e.getMessage());
		} finally {
			JdbcUtil.closeConnection();
		}

		return fOk;
	}

	// 对数据库表的记录进行更新
	public static boolean update(String updateSQL, Object... args) {
		boolean fOk = false;
		try {
			JdbcUtil.conn = JdbcUtil.openConnection();

			// 取消事务的自动提交事务功能
			JdbcUtil.conn.setAutoCommit(false);

			JdbcUtil.pstmt = JdbcUtil.conn.prepareStatement(updateSQL);
			for (int i = 0; i < args.length; i++) {
				JdbcUtil.pstmt.setObject(i + 1, args[i]);
			}
			// JdbcUtil.stmt = JdbcUtil.conn.createStatement();

			// int ret=JdbcUtil.stmt.executeUpdate(updateSQL);
			int ret = JdbcUtil.pstmt.executeUpdate();
			// 手动提交事务
			JdbcUtil.conn.commit();

			log.info("更新记录成功");

			fOk = ret > 0;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtil.conn.rollback();
				log.info("更新没有成功，回滚到更新之前的状态");
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
			log.error(e.getMessage());
		} finally {

			JdbcUtil.closeConnection();
		}

		return fOk;
	}

	// 向数据库表插入记录
	// 在SQL语句以使用占位？
	public static boolean add(String insertSQL, Object... args) {

		boolean fOk = false;

		try {
			JdbcUtil.conn = JdbcUtil.openConnection();

			// 取消事务的自动提交事务功能
			JdbcUtil.conn.setAutoCommit(false);

			JdbcUtil.pstmt = JdbcUtil.conn.prepareStatement(insertSQL);// insert
																		// into
																		// user
																		// (username,password)
																		// values(?,?)
			// JdbcUtil.stmt = JdbcUtil.conn.createStatement();
			// 设置点位符对象的值
			for (int i = 0; i < args.length; i++) {
				JdbcUtil.pstmt.setObject(i + 1, args[i]);// 给第i+1个点位符设置值
			}

			int ret = JdbcUtil.pstmt.executeUpdate();// 使用预编译语句对象执行更新不能再给SQL参数
			// 手动提交事务
			JdbcUtil.conn.commit();

			log.info("增加记录成功");

			fOk = ret > 0;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtil.conn.rollback();
				log.info("增加没有成功，回滚到增加之前的状态");
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
			log.error(e.getMessage());
		} finally {
			JdbcUtil.closeConnection();
		}

		return fOk;
	}

	// 执行查询语句 select --->返回结果集--->列表集合
	public static List<Object[]> findList(String selectSQL,Object...args) {

		List<Object[]> retList = new ArrayList<Object[]>();
		try {
			JdbcUtil.conn = JdbcUtil.openConnection();
			//JdbcUtil.stmt = JdbcUtil.conn.createStatement();
			//ResultSet rs = JdbcUtil.stmt.executeQuery(selectSQL);
			JdbcUtil.pstmt=JdbcUtil.conn.prepareStatement(selectSQL);
			for (int i = 0; i < args.length; i++) {
				JdbcUtil.pstmt.setObject(i + 1, args[i]);// 给第i+1个点位符设置值
			}
			ResultSet rs = JdbcUtil.pstmt.executeQuery();
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

	
	
	//定义增加的ORM方法：约定表主键字段=id
	public static boolean add(Class<?>clazz,Object...args){
		
		boolean fOk=false;
		
		String table=ReflectUtil.getTableName(clazz);
		List<String> fieldList=ReflectUtil.getTableFieldList(clazz);
		//构建Insert SQL语句
		StringBuilder sb=new StringBuilder();
		sb.append("INSERT INTO `").append(table).append("` ( ");
		int count=0;
		for(int i=0;i<fieldList.size();i++){
			String field=fieldList.get(i);
			if(field.equals("id")){//排除表的主键字段
				continue;
			}
			sb.append("`").append(field).append("`,");
			count++;
		}
		sb.delete(sb.length()-1, sb.length());
		sb.append(") VALUES ( ");
		for(int i=0;i<count;i++){
			sb.append("?").append(",");
		}
		sb.delete(sb.length()-1, sb.length());
		sb.append(" )");
		log.info("构建了SQL语句："+sb.toString());
		
		fOk=JdbcUtil.add(sb.toString(), args);
		
		return fOk;
	}
	//使用ORM面向对象的方法操作数据库
    //数据表的名称与类的名称一致（首字符大写）
    //表中各个字段名称与类对象的getXxx(setXxx)中的Xxx名称一致(首字符小写)
	@SuppressWarnings("unchecked")
	public static <T> List<T> findList(Class<?> clazz,Object...args){
		List<T> retList=new ArrayList<T>();
		
		//从类名称获取表的名称
		String table=ReflectUtil.getTableName(clazz);
		
		//获取表的各个字段名称
		List<String>fieldList=ReflectUtil.getTableFieldList(clazz);
		log.info("从反射类中提供表名、字段名及每一个字段对应的类型与set方法");
		//构建SELECT SQL语句
		StringBuilder sb=new StringBuilder();
		sb.append("SELECT ");
		for(int i=0;i<fieldList.size();i++){
			sb.append('`').append(fieldList.get(i)).append('`').append(",");
		}
		//去掉最后的逗号
		sb=sb.delete(sb.length()-1, sb.length());
		sb.append(" FROM ").append(table);
		
		log.info("构建了SQL语句："+sb.toString());
		//
		
		try {
			JdbcUtil.conn=JdbcUtil.openConnection();
		    JdbcUtil.pstmt=	JdbcUtil.conn.prepareStatement(sb.toString());
		    for(int i=0;i<args.length;i++){
		    	pstmt.setObject(i+1, args[i]);
		    } 
		    ResultSet rs=JdbcUtil.pstmt.executeQuery();
		    log.info("获取查询结果集");
		    while(rs.next()){
		    	
		    	//创建类的对象
		    	T o=(T)clazz.newInstance();
		    	for(int i=0;i<fieldList.size();i++){
		    		String field=fieldList.get(i);
		    		Class<?> fieldType=ReflectUtil.getJavaBeanFieldType(clazz, field);
		    		
		    		//获取每记录中对应字段的值
		    		Object value=rs.getObject(field);
		    		
		    		//获取Object的子对象
		    		if(fieldType==String.class){		    			
		    		   value=rs.getString(field);
		    		}else if(fieldType==Integer.class){
		    			value=rs.getInt(field);		    			
		    		}else if(fieldType==Boolean.class){
		    			value=rs.getBoolean(field);		    			
		    		}
		    		
		    		//通过调用对象的setXxx方法为对象赋值
		    		Method m=ReflectUtil.getJavaBeanSetMethod(clazz, field);
		    		m.invoke(o, value);
		    		
		    	}//end for
		    	
		    	//将对象加入返回列表中
		    	retList.add(o);
		    }
		    log.info("获取查询的对象列表:"+retList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}finally{
			JdbcUtil.closeConnection();
		}
		
		return retList;
	}
	
	// 关闭已经打开的连接对象
	public static void closeConnection() {

		// 首先关闭语句对象
		if (JdbcUtil.pstmt != null) {
			try {
				JdbcUtil.pstmt.close();
				JdbcUtil.pstmt = null;// 已经关闭标志
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

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
				// 设置为自动事务提交
				JdbcUtil.conn.setAutoCommit(true);
				JdbcUtil.conn.close();
				JdbcUtil.conn = null;// 清空，以便标志下一次需要创建新连接对象
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

}
