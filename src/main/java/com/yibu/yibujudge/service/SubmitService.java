package com.yibu.yibujudge.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yibu.yibujudge.cache.SubmitCacheService;
import com.yibu.yibujudge.constant.ProblemConstants;
import com.yibu.yibujudge.constant.SubmitConstants;
import com.yibu.yibujudge.constant.UserConstant;
import com.yibu.yibujudge.enumerate.SubmitStatusCode;
import com.yibu.yibujudge.exceptions.BaseException;
import com.yibu.yibujudge.mapper.ContestMapper;
import com.yibu.yibujudge.mapper.ProblemMapper;
import com.yibu.yibujudge.mapper.SubmitMapper;
import com.yibu.yibujudge.mapper.UserMapper;
import com.yibu.yibujudge.model.entity.Contest;
import com.yibu.yibujudge.model.entity.Language;
import com.yibu.yibujudge.model.entity.Problem;
import com.yibu.yibujudge.model.entity.Submit;
import com.yibu.yibujudge.model.entity.SubmitHeat;
import com.yibu.yibujudge.model.entity.SubmitStatus;
import com.yibu.yibujudge.model.entity.SubmitPage;
import com.yibu.yibujudge.model.entity.Testcase;
import com.yibu.yibujudge.model.entity.ContestLeaderboard;
import com.yibu.yibujudge.model.entity.ContestProblems;
import com.yibu.yibujudge.model.mq.SubmitMQType;
import com.yibu.yibujudge.model.response.JudgeResult;
import com.yibu.yibujudge.model.response.PageBean;
import com.yibu.yibujudge.model.response.Result;
import com.yibu.yibujudge.model.vo.SubmitCountVO;
import com.yibu.yibujudge.model.vo.SubmitVO;
import com.yibu.yibujudge.utils.BaseContext;
import com.yibu.yibujudge.utils.DataConvertUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubmitService {

    @Value("${judge.judge-queue}")
    private String mqName;

    private final SubmitMapper submitMapper;
    private final ProblemCaseService problemCaseService;
    private final JudgeService judgeService;
    private final SubmitCacheService submitCacheService;
    private final ProblemMapper problemMapper;
    private final RabbitTemplate rabbitTemplate;

    private final ContestMapper contestMapper;

    private final UserMapper userMapper;

    public SubmitService(SubmitMapper submitMapper,
                         ProblemCaseService problemCaseService, JudgeService judgeService,
                         SubmitCacheService submitCacheService,
                         ProblemMapper problemMapper,
                         RabbitTemplate rabbitTemplate,
                         ContestMapper contestMapper,
                         UserMapper userMapper) {
        this.submitMapper = submitMapper;
        this.problemCaseService = problemCaseService;
        this.judgeService = judgeService;
        this.submitCacheService = submitCacheService;
        this.problemMapper = problemMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.contestMapper = contestMapper;
        this.userMapper = userMapper;
    }

    public Map<Integer, SubmitStatus> submitStatus(List<Integer> problemIds, Long uid) {
        // 获取problemId对应的提交数量和ac数量
        List<SubmitStatus> submitStatuses = submitMapper.getSubmitStatusByProblemIds(problemIds, uid);
        // 转换为Map
        return submitStatuses.stream().collect(Collectors.toMap(SubmitStatus::getProblemId, submitStatus -> submitStatus));
    }

    public PageBean<SubmitPage> getSubmitList(Integer page, Integer size, Integer problemId) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        // 获取提交列表
        PageHelper.startPage(page, size);
        Page<SubmitPage> submitPage = submitMapper.getSubmitByUidAndProblemId(uid, problemId);
        return new PageBean<>(submitPage.getTotal(), submitPage.getResult());
    }

    public Integer getSubmitCount(Integer problemId) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        return submitMapper.getSubmitCount(problemId, uid);
    }

    public Page<SubmitPage> getRecentSubmit(Integer page, Integer size) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        PageHelper.startPage(page, size);
        return submitMapper.getRecentSubmit(uid);
    }

    public SubmitVO getSubmit(Long submitId) {
        Submit submit = submitMapper.getSubmitById(submitId);
        SubmitVO submitVO = BeanUtil.copyProperties(submit, SubmitVO.class);
        try (FileInputStream fileInputStream = new FileInputStream(submit.getCodePath())) {
            String code = IoUtil.readUtf8(fileInputStream);
            submitVO.setCode(code);
        } catch (Exception e) {
            throw new BaseException(SubmitConstants.SYSTEM_ERROR);
        }
        return submitVO;
    }



    public Map<String, Long> getHeatmap(Integer year) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        List<SubmitHeat> submitHeats = submitMapper.getHeatmap(year, uid);
        return submitHeats.stream().collect(Collectors.toMap(SubmitHeat::getSubmitTime, SubmitHeat::getSubmitCount));
    }

    public Result<JudgeResult> debugSubmit(Integer langId, String code, String inputData) {
        Language language = submitMapper.getLanguageById(langId);
        if (language == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        // 调用judgeService进行调试
        JudgeResult judgeResult = judgeService.debug(language.getName(), code, inputData);
        judgeResult.setStatus(SubmitStatusCode.getSubmitStatusCodeByJudgeStatusCode(judgeResult.getStatus()).getSubmitCode());
        return Result.success(judgeResult);
    }

    @Autowired
    private HttpServletRequest request;

    @Transactional
    public Long submit(Integer problemId, Integer langId, String code, Integer contestId) {
        Long uid = BaseContext.getCurrentId();
        if (uid == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        Problem problem = problemMapper.getProblemById(problemId);
        if (problem == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        // 判断是否为比赛题目
        if (Objects.equals(problem.getAuth(), ProblemConstants.CONTEST_AUTH)) {
            // 获取比赛信息
            Contest contest = contestMapper.getContestByProblemId(problemId);
            contestId = contest.getId();
            // 竞赛还没开始
            if (contest.getStartTime().isAfter(LocalDateTime.now())) {
                // 判断是否为管理员
                Object authObject = request.getAttribute(UserConstant.ROLE);
                // 如果不是管理员，则抛出异常
                if (authObject == null || Integer.valueOf(authObject.toString()).compareTo(UserConstant.ROLE_ADMIN) != 0) {
                    throw new BaseException(ProblemConstants.PROBLEM_NOT_FOUND); // 无权限查看
                }
            }
            // 如果正在比赛中， 判断是否报名
            if (contest.getEndTime().isAfter(LocalDateTime.now()) && contest.getStartTime().isBefore(LocalDateTime.now())) { // 竞赛正在进行中
                // 判断是否报名
                ContestLeaderboard rank = contestMapper.getRankByContestIdAndUserId(contestId, uid);
                // 如果没有报名，则抛出异常
                if (rank == null) {
                    throw new BaseException("你还没有报名，请先报名参加比赛");
                }
            }
        }
        Language language = submitMapper.getLanguageById(langId);
        if (language == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        Long id = IdUtil.getSnowflake(1L, 1L).nextId();
        Submit submit = new Submit(id, uid, problemId, contestId, 0, null, langId,
                language.getName(), null, null, null, null, LocalDateTime.now());
        int result = submitMapper.insert(submit);
        if (result == 0) {
            throw new BaseException(SubmitConstants.SYSTEM_ERROR);
        }
        // 设置为等待状态
        submitCacheService.saveSubmitRecord(id);
        // 获取用例数据
        List<Testcase> testcaseList = problemCaseService.getTestCasesByProblemId(problemId, -1);
        rabbitTemplate.convertAndSend(mqName, new SubmitMQType(id, problem, language.getName(), code, uid, contestId, testcaseList));
        return id;
    }

    // 更新判题结果
    @Transactional
    public void updateSubmitResult(Long submitId, JudgeResult judgeResult) {
        Submit submit = submitMapper.getSubmitById(submitId);
        if (submit == null) {
            throw new BaseException(SubmitConstants.ILLEGAL_REQUEST);
        }
        // 更新提交结果
        Submit dbSubmit = new Submit();
        dbSubmit.setId(submitId);
        dbSubmit.setCodePath(judgeResult.getCodePath());
        SubmitStatusCode statusCode = SubmitStatusCode.getSubmitStatusCodeByJudgeStatusCode(judgeResult.getStatus());
        dbSubmit.setStatus(statusCode.getSubmitCode());
        dbSubmit.setRuntime(judgeResult.getCpuTime());
        dbSubmit.setMemory(judgeResult.getMemory());
        dbSubmit.setResultMessage(judgeResult.getMessage());
        if (judgeResult.getCaseCount() != null) {
            submit.setAcCount(judgeResult.getAcCaseCount() + "/" + judgeResult.getCaseCount());
        }
        submitMapper.updateSubmitResult(dbSubmit);
        if (statusCode == SubmitStatusCode.ACCEPTED) {
            // 更新题目提交数
            problemMapper.updateSubmitCount(submit.getProblemId(), 1);
            // 更新用户提交数
            userMapper.updateSubmitCount(submit.getUserId(), 1);
        }
        if (submit.getContestId() != null && statusCode == SubmitStatusCode.ACCEPTED) { // 竞赛题目, 只有ac才更新排名
            List<SubmitStatus> submitStatusList = submitMapper.getSubmitStatusByProblemIds(List.of(submit.getProblemId()), submit.getUserId());
            SubmitStatus status = submitStatusList.get(0);
            if (status.getAcCount() > 0) {
                return;
            }
            // 获取比赛信息
            Contest contest = contestMapper.getContestById(submit.getContestId());
            LocalDateTime startTime = contest.getStartTime();
            LocalDateTime submitTime = submit.getSubmitTime();
            // 计算提交时间间隔, 就是最后一次提交的时间
            long interval = (submitTime.toEpochSecond(ZoneOffset.of("+8")) - startTime.toEpochSecond(ZoneOffset.of("+8"))) / 1000; // 单位秒
            // 获取提交次数， 计算罚时 并更新排名
            int warnCount = Math.max(status.getTotalSubmissions() - 1, 0);
            long penalty = 5L * 60 * warnCount;

            String time = DataConvertUtil.getTime(interval);
            String penaltyTime = DataConvertUtil.getTime(penalty);
            log.info("用户{}在比赛{}中提交题目{}，提交时间{}， 耗时{}， 罚时{}",
                    submit.getUserId(), submit.getContestId(), submit.getProblemId(), submit.getSubmitTime(), time, penaltyTime);
            ContestProblems contestProblems = contestMapper.getContestProblem(submit.getContestId(),
                    submit.getProblemId());
            contestMapper.updateContestRank(submit.getUserId(), submit.getProblemId(), submit.getContestId(),
                    contestProblems.getScore(), contestProblems.getProblemOrder(), time, penaltyTime, penalty, interval, warnCount);
        }
        submitCacheService.deleteSubmitRecord(submitId);
    }

    public void deleteSubmitByProblemIds(List<Integer> problemIds) {
        if (problemIds == null || problemIds.isEmpty()) {
            return;
        }
        submitMapper.deleteSubmitByProblemIds(problemIds);
    }

    public SubmitVO resultApi(Long id) {
        if (submitCacheService.isSubmitRecordExists(id)) { // 等待结果中
            return null;
        }
        Submit submit = submitMapper.getSubmitById(id);
        return BeanUtil.copyProperties(submit, SubmitVO.class);
    }

    public SubmitCountVO getSubmitCountByUser(Long userId) {
        // 获取用户提交数的ac数量
        List<Integer> problemIds =  submitMapper.getAllAcceptedProblemIds(userId);
        // 获取题目难度
        List<Problem> problems = null;
        if (problemIds == null || problemIds.isEmpty()) {
            problems = problemMapper.getProblemByIds(problemIds);
        }
        SubmitCountVO submitCountVO = new SubmitCountVO();
        if (problems == null || problems.isEmpty()) {
            return submitCountVO;
        }
        submitCountVO.setSubmitCount(
                problems.stream().collect(Collectors.groupingBy(Problem::getDifficulty, Collectors.counting())));
        // 通过总数计算ac数量
        submitCountVO.setAcCount(problemIds.size());
        return submitCountVO;
    }
}
