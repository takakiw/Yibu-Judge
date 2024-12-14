package com.yibu.yibuJudge.mapper;

import com.github.pagehelper.Page;
import com.yibu.yibuJudge.model.entity.Contest;
import com.yibu.yibuJudge.model.entity.ContestLeaderboard;
import com.yibu.yibuJudge.model.entity.ContestProblems;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContestMapper {
    int insertContest(Contest dbContest);

    int insertContestProblems(List<ContestProblems> contestProblems);

    @Delete("DELETE FROM contest_problems WHERE contest_id = #{contestId}")
    void deleteContestProblemsByContestId(@Param("contestId") Integer contestId);

    @Delete("DELETE FROM contest WHERE id = #{contestId}")
    void deleteContestById(@Param("contestId") Integer contestId);


    List<Integer> getContestProblemsByContestId(@Param("contestId") Integer contestId);

    @Select("SELECT * FROM contest WHERE id = #{contestId}")
    Contest getContestById(@Param("contestId") Integer contestId);

    int updateContest(Contest dbContest);

    Page<Contest> listContest(@Param("preparation") Boolean preparation);


    ContestLeaderboard getRankByContestIdAndUserId(Integer contestId, Long userId);

    int insertContestLeaderboard(ContestLeaderboard dbContestLeaderboard);

    @Delete("DELETE FROM contest_leaderboard WHERE contest_id = #{contestId} AND user_id = #{userId}")
    int deleteContestLeaderboardByContestIdAndUserId(@Param("contestId") Integer contestId, @Param("userId") Long userId);

    Page<ContestLeaderboard> getRankListByContestId(@Param("contestId") Integer contestId);

    @Select("select * from contest_problems where contest_id = #{contestId} order by problem_order")
    List<ContestProblems> getContestProblemIds(Integer contestId);


    @Delete("select c.start_time, c.end_time from contest_problems cl left join contest c on cl.contest_id = c.id where problem_id = #{problemId}")
    Contest getContestByProblemId(Integer problemId);

    @Select("select problem_order, score from contest_problems where contest_id = #{contestId} and problem_id = #{problemId}")
    ContestProblems getContestProblem(Integer contestId, Integer problemId);

    void updateContestRank(@Param("userId") Long userId, @Param("problemId") Integer problemId, @Param("contestId") Integer contestId,
                           @Param("score") Integer score, @Param("problemOrder") Integer problemOrder, @Param("time") String time,
                           @Param("penaltyTime") String penaltyTime, @Param("penalty") long penalty, @Param("interval") long interval, @Param("warnCount") int warnCount);
}
