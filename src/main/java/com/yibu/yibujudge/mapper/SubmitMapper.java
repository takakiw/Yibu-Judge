package com.yibu.yibujudge.mapper;

import com.github.pagehelper.Page;
import com.yibu.yibujudge.model.entity.SubmitPage;
import com.yibu.yibujudge.model.entity.Submit;
import com.yibu.yibujudge.model.entity.SubmitStatus;
import com.yibu.yibujudge.model.entity.SubmitHeat;
import com.yibu.yibujudge.model.entity.Language;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SubmitMapper {
    List<SubmitStatus> getSubmitStatusByProblemIds(@Param("problemIds") List<Integer> problemIds, @Param("uid") Long uid);

    @Select("SELECT s.id, s.status, s.runtime,s.submit_time, l.name as languageName , s.problem_id, s.memory "
            + "FROM submit s left join language l on s.lang_id = l.id "
            + "WHERE s.user_id = #{uid} AND s.problem_id = #{problemId} ORDER BY s.submit_time DESC")
    Page<SubmitPage> getSubmitByUidAndProblemId(@Param("uid") Long uid, @Param("problemId") Integer problemId);

    @Select("SELECT COUNT(*) FROM submit WHERE problem_id = #{problemId} AND user_id = #{uid}")
    Integer getSubmitCount(@Param("problemId") Integer problemId, @Param("uid") Long uid);

    @Select("select s.id, s.status, s.problem_id, s.runtime,s.submit_time, l.name as languageName, s.memory "
            + "from submit s left join language l on s.lang_id = l.id "
            + "where s.user_id = #{uid} order by s.submit_time desc")
    Page<SubmitPage> getRecentSubmit(@Param("uid") Long uid);

    @Select("select s.*, l.name as languageName from submit s left join language l on s.lang_id = l.id where s.id = #{submitId}")
    Submit getSubmitById(@Param("submitId") Long submitId);

    @Select("select date_format(submit_time, '%Y-%m-%d') as submitTime, count(*) as submitCount "
            + "from submit where user_id = #{uid} and year(submit_time) = #{year} group by date_format(submit_time, '%Y-%m-%d')")
    List<SubmitHeat> getHeatmap(@Param("year") Integer year, @Param("uid") Long uid);

    @Select("select name, suffix from language where id = #{langId}")
    Language getLanguageById(@Param("langId") Integer langId);

    int insert(Submit submit);

    void deleteSubmitByProblemIds(@Param("problemIds") List<Integer> problemIds);

    @Update("update submit set status = #{status}, runtime = #{runtime}, memory = #{memory}, result_message = #{resultMessage}, code_path = #{codePath} "
            + "where id = #{submitId}")
    void updateSubmitResult(Submit dbSubmit);

    @Select("select problem_id from submit where status = 1 and user_id = #{userId} group by problem_id")
    List<Integer> getAllAcceptedProblemIds(@Param("userId") Long userId);
}
