package com.ilib.dao;

import org.apache.ibatis.annotations.Param;

import com.ilib.model.User;

public interface UserDao {
	/**
	 * 用户信息
	 * @param username
	 * @throws Exception
	 */
	User getUserByUsername(@Param(value="username") String username)throws Exception;
	/**
	 * 用户详细信息
	 * @param username
	 * @throws Exception
	 */
	User getUserDetailByUsername(@Param(value="username") String username)throws Exception;
	String getNameByID(Long id)throws Exception;
}
