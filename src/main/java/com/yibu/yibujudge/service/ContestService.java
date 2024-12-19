package com.yibu.yibujudge.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yibu.yibujudge.exceptions.BaseException;
import com.yibu.yibujudge.mapper.ContestMapper;
import com.yibu.yibujudge.model.dto.ContestDTO;
import com.yibu.yibujudge.model.dto.ProblemDTO;
import com.yibu.yibujudge.model.entity.Contest;
import com.yibu.yibujudge.model.entity.ContestLeaderboard;
import com.yibu.yibujudge.model.entity.ContestProblems;
import com.yibu.yibujudge.model.response.PageBean;
import com.yibu.yibujudge.model.vo.ContestVO;
import com.yibu.yibujudge.utils.BaseContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContestService {

    private final ContestMapper contestMapper;

    private final ProblemService problemService;

    public ContestService(ContestMapper contestMapper,
                          ProblemService problemService) {
        this.contestMapper = contestMapper;
        this.problemService = problemService;
    }

    @Transactional
    public void createContest(ContestDTO contestDTO) {
        // 保存比赛
        Contest dbContest = new Contest(
                null,
                contestDTO.getName(),
                contestDTO.getDescription(),
                contestDTO.getStartTime(),
                contestDTO.getEndTime(),
                contestDTO.getOrganizer(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Contest existingContest = contestMapper.getContestTitle(contestDTO.getName());
        if (existingContest != null) {
            throw new BaseException("比赛名称已存在");
        }
        int result = contestMapper.insertContest(dbContest);
        if (result == 0) {
            throw new RuntimeException("创建比赛失败");
        }
        // 获取题目列表
        List<ProblemDTO> problems = contestDTO.getProblems();
        // 保存题目
        List<Integer> problemIds = problemService.addProblems(problems);

        Integer contestId = dbContest.getId();
        // 保存比赛题目关系
        List<ContestProblems> contestProblemsList = new ArrayList<>();
        for (int i = 0; i < problemIds.size(); i++) {
            ProblemDTO problem = problems.get(i);
            ContestProblems contestProblems = new ContestProblems(
                    null,
                    problem.getTitle(),
                    contestId,
                    problemIds.get(i),
                    problem.getOrder(),
                    problem.getScore(),
                    null);
            contestProblemsList.add(contestProblems);
        }
        contestMapper.insertContestProblems(contestProblemsList);
    }

    @Transactional
    public void deleteContest(Integer contestId, Boolean force) {
        Contest contest = contestMapper.getContestById(contestId);
        if (contest == null) {
            throw new BaseException("比赛不存在");
        }
        if (contest.getStartTime().isAfter(LocalDateTime.now()) && contest.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BaseException("正在进行的比赛无法删除");
        }
        contestMapper.deleteContestById(contestId);
        // 获取比赛题目列表
        List<Integer> problems = contestMapper.getContestProblemsByContestId(contestId);
        // 删除比赛后， 将题目设置为公开
        if (!force) {
            problemService.setAuthPublicByIds(problems);
        } else {
            problemService.deleteProblem(problems);
        }
        contestMapper.deleteContestProblemsByContestId(contestId);
    }

    public void updateContest(Integer id, String name, String description, LocalDateTime startTime, LocalDateTime endTime, String organizer) {
        Contest contestById = contestMapper.getContestById(id);
        if (contestById == null) {
            throw new BaseException("比赛不存在");
        }
        Contest dbContest = new Contest(
                id,
                name,
                description,
                startTime,
                endTime,
                organizer,
                null,
                LocalDateTime.now()
        );
        int result = contestMapper.updateContest(dbContest);
        if (result == 0) {
            throw new BaseException("更新比赛失败");
        }
    }

    public PageBean<Contest> listContest(Integer page, Integer size, Boolean preparation) {
        PageHelper.startPage(page, size);
        Page<Contest> contests = contestMapper.listContest(preparation);
        return new PageBean<>(contests.getTotal(), contests.getResult());
    }

    public void registerContest(Integer contestId) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BaseException("请先登录");
        }
        // 判断是否已经注册
        ContestLeaderboard contestLeaderboard =  contestMapper.getRankByContestIdAndUserId(contestId, userId);
        if (contestLeaderboard != null) {
            throw new BaseException("您已经注册过该比赛");
        }
        // 注册比赛
        ContestLeaderboard dbContestLeaderboard = new ContestLeaderboard(
                null,
                contestId,
                userId,
                null,
                0L,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                null);
        int result = contestMapper.insertContestLeaderboard(dbContestLeaderboard);
        if (result == 0) {
            throw new BaseException("注册比赛失败");
        }
    }

    public void unregisterContest(Integer contestId) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BaseException("请先登录");
        }
        // 判断是否已经注册
        ContestLeaderboard contestLeaderboard =  contestMapper.getRankByContestIdAndUserId(contestId, userId);
        if (contestLeaderboard == null) {
            throw new BaseException("您没有注册过该比赛");
        }
        // 注销比赛
        int result = contestMapper.deleteContestLeaderboardByContestIdAndUserId(contestId, userId);
        if (result == 0) {
            throw new BaseException("注销比赛失败");
        }
    }

    public List<ContestLeaderboard> getRankList(Integer contestId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return contestMapper.getRankListByContestId(contestId).getResult();
    }

    public ContestLeaderboard getRank(Integer contestId) {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BaseException("请先登录");
        }
        ContestLeaderboard contestLeaderboard =  contestMapper.getRankByContestIdAndUserId(contestId, userId);
        if (contestLeaderboard == null) {
            throw new BaseException("您没有注册过该比赛");
        }
        return contestLeaderboard;
    }

    public ContestVO getContestInfo(Integer contestId) {
        // 获取比赛信息
        Contest contest = contestMapper.getContestById(contestId);
        if (contest == null) {
            throw new BaseException("比赛不存在");
        }
        ContestVO contestVO = BeanUtil.copyProperties(contest, ContestVO.class);
        Long userId = BaseContext.getCurrentId();
        ContestLeaderboard rank = contestMapper.getRankByContestIdAndUserId(contestId, userId);
        contestVO.setRegister(rank != null);
        if (contest.getStartTime().isAfter(LocalDateTime.now()) || rank == null) {
            return contestVO; // 未开始或未注册，不显示题目
        }
        // 获取比赛题目列表
        List<ContestProblems> contestProblems = contestMapper.getContestProblemIds(contestId);
        contestVO.setContestProblems(contestProblems);
        return contestVO;
    }
}
