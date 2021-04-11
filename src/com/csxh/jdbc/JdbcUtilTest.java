package com.csxh.jdbc;

import java.util.List;
import java.sql.Connection;

import org.junit.Assert;
import org.junit.Test;


public class JdbcUtilTest {

	@Test
	public void testOpenConnection() {
	  Connection conn=JdbcUtil.openConnection();
	  Assert.assertTrue(conn!=null);
	  JdbcUtil.closeConnection();
	}

	@Test
	public void testExcute() {
		String sql="INSERT INTO `user` (`username`,`password`) VALUES ( 'yyy','1345'  )";
		boolean b=JdbcUtil.excute(sql);
		Assert.assertTrue(b);
	}
	@Test
	public void testFind() {
		String sql="SELECT * FROM `user`";
		List<Object[]>list=JdbcUtil.findList(sql);
		Assert.assertTrue(list.size()>0);
	}

}
