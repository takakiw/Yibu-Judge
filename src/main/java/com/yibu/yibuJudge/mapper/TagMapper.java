package com.yibu.yibuJudge.mapper;

import com.yibu.yibuJudge.model.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagMapper {
    Integer insert(String name);

    @Select("SELECT * FROM tags WHERE name = #{name}")
    Tag getTagByName(@Param("name") String name);

    int delete(@Param("ids") List<Integer> ids);

    List<Tag> getUseingTag(List<Integer> ids);

    @Select("select * from tags where id = #{id}")
    Tag getTagById(@Param("id") Integer id);

    int update(Tag tag);

    @Select("SELECT * FROM tags")
    List<Tag> list();

    List<Integer> getTagIdByIds(@Param("tagIds") List<Integer> tagIds);
}
