package com.yibu.yibujudge.mapper;

import com.yibu.yibujudge.model.entity.CodeTemplate;
import com.yibu.yibujudge.model.entity.Language;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CodeTemplateMapper {

    @Select("SELECT id, problem_id, language_id, template_code FROM code_template WHERE problem_id = #{problemId} and language_id = #{langId}")
    CodeTemplate getProblemCodeTemplateByProblemId(@Param("problemId") Integer problemId, @Param("langId") Integer langId);

    @Select("SELECT name, suffix FROM language WHERE id = #{langId}")
    Language getLanguageById(@Param("langId") Integer langId);

    int insert(CodeTemplate dbCodeTemplate);

    @Select("select * from code_template where id = #{id}")
    CodeTemplate getProblemCodeTemplateById(@Param("id") Integer id);

    int updateCodeTemplate(CodeTemplate codeTemplate);

    @Delete("delete from code_template where id = #{id}")
    void deleteById(@Param("id") Integer id);

    void deleteByProblemIds(@Param("problemIds") List<Integer> problemIds);
}
