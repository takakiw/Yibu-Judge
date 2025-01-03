package com.yibu.yibujudge.mapper;

import com.github.pagehelper.Page;
import com.yibu.yibujudge.model.entity.Language;
import com.yibu.yibujudge.model.entity.Problem;
import com.yibu.yibujudge.model.entity.ProblemPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProblemMapper {
    Page<ProblemPage> getProblemList(@Param("tags") List<String> tags, @Param("tagSize") int tagSize, @Param("title") String title,
                                     @Param("difficulty") Integer difficulty, @Param("order") String order, @Param("sort") String sort);

    Problem getProblemById(@Param("id") Integer id);

    Integer insert(Problem problem);

    @Select("select count(1) from problems where auth = 0")
    Integer getProblemCount();

    int deleteProblemBatch(List<Integer> ids);

    int update(Problem dbProblem);

    @Select("select * from language;")
    List<Language> getLanguageList();

    int saveProblemTags(@Param("tagIds") List<Integer> tagIds, @Param("id") Integer id);

    @Select("select * from language where id = #{languageId}")
    Language getLanguageById(@Param("languageId") Integer languageId);

    void deleteProblemTags(@Param("problemId") Integer problemId);

    void setAuthPublicByIds(@Param("ids") List<Integer> ids);

    List<Problem> getProblemByTitle(@Param("titles") List<String> titles);

    @Update("update problems set submit_count = submit_count + 1, accepted_count = accepted_count + #{i} where id = #{problemId}")
    void updateSubmitCount(@Param("problemId") Integer problemId, @Param("isAccepted") int isAccepted);

    List<Problem> getProblemByIds(@Param("problemIds") List<Integer> problemIds);
}
