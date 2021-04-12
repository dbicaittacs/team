package com.csxh.jdbc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

/*
 * Jdbc����������
 */
public class JdbcUtil {

	// ������־����
	private static Logger log = Logger.getLogger(JdbcUtil.class);
	// ����jdbc���Ӳ��������ӵ�url���û���user������password
	private static String url;
	private static String user;
	private static String password;
	private static Connection conn;
	private static Statement stmt;
	private static PreparedStatement pstmt;

	static {
		try {
			// 1��ʹ��PropertY���ȡjdbc�����ò���
			Properties p = new Properties();
			InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
			// InputStream
			// in2=JdbcUtil.class.getResourceAsStream("/jdbc.properties");
			p.load(in);
			in.close();
			JdbcUtil.url = p.getProperty("url");
			JdbcUtil.user = p.getProperty("user");
			JdbcUtil.password = p.getProperty("password");
			log.info("��ȡjdbc���ò������");
			// 2��ʹ�÷��������jdbc��������
			Class.forName("com.mysql.jdbc.Driver");
			JdbcUtil.conn = null;
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

	// 3��ͨ����������������������һ�����Ӷ���
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

	// 4��ʹ�����Ӷ��󴴽�SQL������,���������������SQL���
	public static boolean excute(String sql) {

		boolean b = true;
		try {
			// 4.1�����Ӷ���
			JdbcUtil.conn = JdbcUtil.openConnection();
			JdbcUtil.stmt = conn.createStatement();
			log.info("����������ɹ�");
			// 4.3ͨ��������ִ��SQL���
			JdbcUtil.stmt.execute(sql);

			log.info("ִ��SQL���");
			// 4.5����ִ�еĽ��
			return b;

		} catch (SQLException e) {
			log.error(e.getMessage());
			b = false;
		} finally {
			// 4.4 �ر����Ӷ���
			JdbcUtil.closeConnection();
		}

		return b;

	}

	// �����ݿ��¼����ɾ��
	public static boolean delete(String deleteSQL,Object...args) {

		boolean fOk = false;
		try {
			JdbcUtil.conn = JdbcUtil.openConnection();

			// ȡ��������Զ��ύ������
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
			// �ֶ��ύ����
			JdbcUtil.conn.commit();

			log.info("ɾ����¼�ɹ�");

			fOk = ret > 0;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtil.conn.rollback();
				log.info("ɾ��û�гɹ����ع���ɾ��֮ǰ��״̬");
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
			log.error(e.getMessage());
		} finally {
			JdbcUtil.closeConnection();
		}

		return fOk;
	}

	// �����ݿ��ļ�¼���и���
	public static boolean update(String updateSQL, Object... args) {
		boolean fOk = false;
		try {
			JdbcUtil.conn = JdbcUtil.openConnection();

			// ȡ��������Զ��ύ������
			JdbcUtil.conn.setAutoCommit(false);

			JdbcUtil.pstmt = JdbcUtil.conn.prepareStatement(updateSQL);
			for (int i = 0; i < args.length; i++) {
				JdbcUtil.pstmt.setObject(i + 1, args[i]);
			}
			// JdbcUtil.stmt = JdbcUtil.conn.createStatement();

			// int ret=JdbcUtil.stmt.executeUpdate(updateSQL);
			int ret = JdbcUtil.pstmt.executeUpdate();
			// �ֶ��ύ����
			JdbcUtil.conn.commit();

			log.info("���¼�¼�ɹ�");

			fOk = ret > 0;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtil.conn.rollback();
				log.info("����û�гɹ����ع�������֮ǰ��״̬");
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
			log.error(e.getMessage());
		} finally {

			JdbcUtil.closeConnection();
		}

		return fOk;
	}

	// �����ݿ������¼
	// ��SQL�����ʹ��ռλ��
	public static boolean add(String insertSQL, Object... args) {

		boolean fOk = false;

		try {
			JdbcUtil.conn = JdbcUtil.openConnection();

			// ȡ��������Զ��ύ������
			JdbcUtil.conn.setAutoCommit(false);

			JdbcUtil.pstmt = JdbcUtil.conn.prepareStatement(insertSQL);// insert
																		// into
																		// user
																		// (username,password)
																		// values(?,?)
			// JdbcUtil.stmt = JdbcUtil.conn.createStatement();
			// ���õ�λ�������ֵ
			for (int i = 0; i < args.length; i++) {
				JdbcUtil.pstmt.setObject(i + 1, args[i]);// ����i+1����λ������ֵ
			}

			int ret = JdbcUtil.pstmt.executeUpdate();// ʹ��Ԥ����������ִ�и��²����ٸ�SQL����
			// �ֶ��ύ����
			JdbcUtil.conn.commit();

			log.info("���Ӽ�¼�ɹ�");

			fOk = ret > 0;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				JdbcUtil.conn.rollback();
				log.info("����û�гɹ����ع�������֮ǰ��״̬");
			} catch (SQLException e1) {
				log.error(e1.getMessage());
			}
			log.error(e.getMessage());
		} finally {
			JdbcUtil.closeConnection();
		}

		return fOk;
	}

	// ִ�в�ѯ��� select --->���ؽ����--->�б���
	public static List<Object[]> findList(String selectSQL,Object...args) {

		List<Object[]> retList = new ArrayList<Object[]>();
		try {
			JdbcUtil.conn = JdbcUtil.openConnection();
			//JdbcUtil.stmt = JdbcUtil.conn.createStatement();
			//ResultSet rs = JdbcUtil.stmt.executeQuery(selectSQL);
			JdbcUtil.pstmt=JdbcUtil.conn.prepareStatement(selectSQL);
			for (int i = 0; i < args.length; i++) {
				JdbcUtil.pstmt.setObject(i + 1, args[i]);// ����i+1����λ������ֵ
			}
			ResultSet rs = JdbcUtil.pstmt.executeQuery();
			log.info("��ȡ��ѯ�Ľ����");
			// ��ȡ��������������ֶ�����
			int columnCount = rs.getMetaData().getColumnCount();
			// �Խ�������е���ȡֵ
			while (rs.next()) {
				// ����Object���飬�Ա����ɸ��еļ�¼ֵ
				Object[] os = new Object[columnCount];
				for (int column = 0; column < columnCount; column++) {
					os[column] = rs.getObject(column + 1);// �е������Ǵ�1��ʼ��
				}
				retList.add(os);
			}
			log.info("��������еļ�¼ת�����б�����");

		} catch (SQLException e) {
			log.error(e.getMessage());
		} finally {
			JdbcUtil.closeConnection();
		}

		return retList;

	}

	// �ر��Ѿ��򿪵����Ӷ���
	public static void closeConnection() {

		// ���ȹر�������
		if (JdbcUtil.pstmt != null) {
			try {
				JdbcUtil.pstmt.close();
				JdbcUtil.pstmt = null;// �Ѿ��رձ�־
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		if (JdbcUtil.stmt != null) {
			try {
				JdbcUtil.stmt.close();
				JdbcUtil.stmt = null;// �Ѿ��رձ�־
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}

		// �ٹر����Ӷ���
		if (JdbcUtil.conn != null) {
			try {
				// ����Ϊ�Զ������ύ
				JdbcUtil.conn.setAutoCommit(true);
				JdbcUtil.conn.close();
				JdbcUtil.conn = null;// ��գ��Ա��־��һ����Ҫ���������Ӷ���
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}

}
