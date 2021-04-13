package com.csxh.jdbc;

import java.util.List;
import java.sql.Connection;

import org.junit.Assert;
import org.junit.Test;

import com.csxh.bean.User;


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
	
	@Test
	public void testAdd() {
		//占位符特指定值域
		String sql="INSERT INTO `user` (`username`,`password`) VALUES ( ?, ?)";
		Object[] args=new Object[]{"aaa","bbb"};
		boolean b=JdbcUtil.add(sql,args);
		Assert.assertTrue(b);
	}
	@Test
	public void testAdd2() {
		//占位符特指定值域
		Object[] args=new Object[]{"aaa","bbb"};
		boolean b=JdbcUtil.add(User.class,args);
		Assert.assertTrue(b);
	}
	@Test
	public void testUpdate() {
		String sql="UPDATE `user` SET `username`='zzzz' , `password`='abcd' WHERE `id`=2";
		boolean b=JdbcUtil.update(sql);
		Assert.assertTrue(b);
	}
	@Test
	public void testFindList() {
		List<User>userList=JdbcUtil.findList(User.class);
		Assert.assertTrue(userList.size()>0);
	}

}
