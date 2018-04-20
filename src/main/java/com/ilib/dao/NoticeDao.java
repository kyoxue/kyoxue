package com.ilib.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ilib.model.Notice;

public interface NoticeDao {
	Map<String, Object> getNotice(Integer hour)throws Exception;
	@Select("select id,title,content,enable,createtime,creater from t_notice where id = #{id}")
	Notice getNoticeByPrimarykey(Long id);
	@Select("select title,content from t_notice where title = #{title} order by createtime desc")
	List<Map<String, String>> getNoticeTitleAndContent(@Param("title") String title);
	@Insert("insert into t_notice(title,content,creater) values(#{title},#{content},#{creater})")
	void insertNotice(Notice notice);
	@Select("select id,title,content,enable,createtime,creater from t_notice where title = #{title}")
	List<Notice> getNoticeBy(String title);
}
