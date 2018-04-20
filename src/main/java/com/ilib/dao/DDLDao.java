package com.ilib.dao;

import org.apache.ibatis.annotations.Param;

public interface DDLDao {
	 /**
     * 修改数据库的表名字
     * @param originalTableName
     * @param newTableName
     * @return
     */
    int alterTableName(@Param("originalTableName") String originalTableName,
            @Param("newTableName") String newTableName);
    
    /**
     * truncate指定数据库表的数据
     * @param tableName
     * @return
     */
    int truncateTable(@Param("tableName") String tableName);

    
    /**
     * 根据传入的表明，创建新的表并且将原表的数据插入到新的Occur表中
     * @param newTableName
     * @param originalTableName
     */
    void createNewTableAndInsertData(@Param("newTableName") String newTableName,
            @Param("originalTableName") String originalTableName);
    
    /**
     * 统计某张表中的总数据条数。
     * @param tableName
     * @return 指定表中的总记录条数。
     */
    int getRecordCount(@Param("tableName") String tableName);
    
    /**
     * 获得当前数据库的名字
     * @return
     */
    String getCurDataBaseName();
    
    /**
     * 从指定数据库中，查询是否存在某张表
     * @param dataBaseName
     * @param tableName
     * @return
     */
    String isTargetTableExistInDB(@Param("dataBaseName") String dataBaseName, 
            @Param("tableName") String tableName);
    
    /**
     * 调存储过程
     */
//    void callProc()throws Exception;
}
