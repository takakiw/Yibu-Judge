package com.yibu.yibuJudge.mapper;


import com.yibu.yibuJudge.model.entity.Testcase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProblemCaseMapper {

    List<Testcase> getTestCasesByProblemId(@Param("id") Integer id, @Param("limit") int limit);

    int insertBatch(@Param("testcaseList") List<Testcase> testcaseList);

    List<Testcase> getTestCasesByProblemIdBatch(@Param("problemIds") List<Integer> problemIds);

    int deleteTestCasesByProblemIds(@Param("problemIds") List<Integer> problemIds);

    @Select("select * from testcases where id = #{id}")
    Testcase getTestcaseById(@Param("id") Long id);

    int deleteTestCasesByIds(@Param("ids") List<Long> ids);

    int update(Testcase testcase);
}
