package com.ilib.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ilib.model.TestBeanB;

public interface TestDao {
	TestBeanB selectUserById(@Param("uid") Integer uid)throws Exception;
	/**
	 * 根据权限查用户
	 */
	List<TestBeanB> selectUserByRole(@Param("role") String role,@Param("type") String type)throws Exception;
	/**
	 * 查询权限分组的对应的权限和用户
	 */
	List<Map<String, String>> selectGroupAndUser(@Param("type") String type)throws Exception;
	/**
	 * 查询权限分组所有的权限
	 */
	List<String> selectRoleByType(@Param("type") String type)throws Exception;
	/**
	 * 查询权限分组所有的用户
	 */
	List<String> selectUserAccountByType(@Param("type") String type)throws Exception;
}
