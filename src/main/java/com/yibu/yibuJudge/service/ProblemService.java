package com.yibu.yibuJudge.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yibu.yibuJudge.constant.ProblemConstants;
import com.yibu.yibuJudge.constant.UserConstant;
import com.yibu.yibuJudge.exceptions.BaseException;
import com.yibu.yibuJudge.mapper.ContestMapper;
import com.yibu.yibuJudge.mapper.ProblemMapper;
import com.yibu.yibuJudge.model.dto.ProblemDTO;
import com.yibu.yibuJudge.model.entity.*;
import com.yibu.yibuJudge.model.vo.ProblemVO;
import com.yibu.yibuJudge.model.vo.TestcaseVO;
import com.yibu.yibuJudge.properties.FileProperties;
import com.yibu.yibuJudge.utils.BaseContext;
import com.yibu.yibuJudge.utils.SubmitStatusUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class ProblemService {

    private final ProblemMapper problemMapper;
    private final SubmitService submitService;
    private final ProblemCaseService problemCaseService;

    private final ContestMapper contestMapper;

    private final TagService tagService;

    private final CodeTemplateService codeTemplateService;
    private final FileProperties fileProperties;

    public ProblemService(ProblemMapper problemMapper,
                          SubmitService submitService,
                          ProblemCaseService problemCaseService,
                          ContestMapper contestMapper, TagService tagsService,
                          CodeTemplateService codeTemplateService,
                          FileProperties fileProperties) {
        this.problemMapper = problemMapper;
        this.submitService = submitService;
        this.problemCaseService = problemCaseService;
        this.contestMapper = contestMapper;
        this.tagService = tagsService;
        this.codeTemplateService = codeTemplateService;
        this.fileProperties = fileProperties;
    }

    public Page<ProblemPage> getProblemList(int page, int size, List<String> tags, String title, String order, String sort) {
        Long uid = BaseContext.getCurrentId();
        page = (page <= 0) ? 1 : page;
        size = (size <= 0 || size > 100) ? 10 : size;
        order = (order == null || (!order.equals("difficulty") && !order.equals("id"))) ? "id" : order;
        sort = (sort == null || (!sort.equalsIgnoreCase("DESC") && !sort.equalsIgnoreCase("ASC"))) ? "ASC" : sort.toUpperCase();
        PageHelper.startPage(page, size);
        Page<ProblemPage> problemList = problemMapper.getProblemList(tags, tags == null ? 0: tags.size(), title, order, sort);
        // 判断提交状态
        List<Integer> id_list = problemList.stream().map(ProblemPage::getId).toList();
        if (uid!= null && !id_list.isEmpty()){
            Map<Integer, SubmitStatus> submit_status = submitService.submitStatus(id_list, uid);
            for (ProblemPage problem : problemList) {
                SubmitStatus submitStatus = submit_status.getOrDefault(problem.getId(), new SubmitStatus());
                problem.setStatus(submitStatus == null? 0 : SubmitStatusUtil.getProblemStatus(submitStatus.getAcCount(), submitStatus.getTotalSubmissions()));
            }
        }
        return problemList;
    }

    @Autowired
    private HttpServletRequest request;

    public ProblemVO getProblem(Integer id) {
        Long uid = BaseContext.getCurrentId();
        Problem problem = problemMapper.getProblemById(id);
        if (problem == null){
            throw new BaseException(ProblemConstants.PROBLEM_NOT_FOUND);
        }
        if (Objects.equals(problem.getAuth(), ProblemConstants.PROBLEM_AUTH_CONTEST)){// 如果是比赛题目，判断是否有权限查看
            // 获取比赛信息
            Contest contest = contestMapper.getContestByProblemId(id);
            if (contest != null && contest.getStartTime().isAfter(LocalDateTime.now())) { // 竞赛还没开始
                // 判断是否为管理员
                Object authObject = request.getAttribute(UserConstant.ROLE);
                // 如果不是管理员，则抛出异常
                if (authObject == null || Integer.valueOf(authObject.toString()).compareTo(UserConstant.ROLE_ADMIN) != 0){
                    throw new BaseException(ProblemConstants.PROBLEM_NOT_FOUND); // 无权限查看
                }
            }
        }
        ProblemVO problemVO = BeanUtil.copyProperties(problem, ProblemVO.class);
        // 获取用例
        List<Testcase> testcases = problemCaseService.getTestCasesByProblemId(id, ProblemConstants.TESTCASE_LIMIT);
        List<TestcaseVO> testcaseVOList = new ArrayList<>();
        testcases.forEach(testcase -> {
            TestcaseVO testcaseVO = BeanUtil.copyProperties(testcase, TestcaseVO.class);
            try(FileInputStream inputStream = new FileInputStream(testcase.getInputPath());
                FileInputStream outputInputStream = new FileInputStream(testcase.getOutputPath()))
                {
                    String inputData = IoUtil.readUtf8(inputStream);
                    testcaseVO.setInputData(inputData);
                    String outputData = IoUtil.readUtf8(outputInputStream);
                    testcaseVO.setOutputData(outputData);
                } catch (Exception e) {
                    throw new BaseException(ProblemConstants.TESTCASE_NOT_FOUND);
                }
            testcaseVOList.add(testcaseVO);
        });
        problemVO.setTestcases(testcaseVOList);
        // 判断提交状态
        if (uid!= null){
            SubmitStatus submitStatus = submitService.submitStatus(List.of(id), uid).get(id);
            problemVO.setStatus(submitStatus == null ? 0 : SubmitStatusUtil.getProblemStatus(submitStatus.getAcCount(), submitStatus.getTotalSubmissions()));
        }
        return problemVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Integer> addProblems(List<ProblemDTO> problems) {
        List<Problem> existProblem = problemMapper.getProblemByTitle(problems.stream().map(ProblemDTO::getTitle).toList());
        if (!existProblem.isEmpty()){
            throw new BaseException(ProblemConstants.PROBLEM_TITLE_EXIST);
        }
        try{
            List<Integer> ids = new ArrayList<>();
            for (ProblemDTO problemDTO : problems) {
                Integer id = addProblem(problemDTO);
                ids.add(id);
            }
            return ids;
        }finally {
            for (ProblemDTO problemDTO : problems){
                FileUtil.del(fileProperties.getCaseInPath() + "/" + problemDTO.getId());
                FileUtil.del(fileProperties.getCaseOutPath() + "/" + problemDTO.getId());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer addProblem(ProblemDTO problemDTO) {
        Problem problem = new Problem();
        problem.setTitle(problemDTO.getTitle());
        problem.setDescription(problemDTO.getDescription());
        problem.setInputDesc(problemDTO.getInputDesc());
        problem.setOutputDesc(problemDTO.getOutputDesc());
        problem.setTimeLimit(problemDTO.getTimeLimit());
        problem.setMemoryLimit(problemDTO.getMemoryLimit());
        problem.setDifficulty(problemDTO.getDifficulty());
        problem.setAuth(problemDTO.getAuth());
        problem.setDataRange(problemDTO.getDataRange());
        problemMapper.insert(problem);
        Integer id = problem.getId();
        if (id == null){
            throw new BaseException(ProblemConstants.PROBLEM_ADD_ERROR);
        }
        // 保存tags
        if (problemDTO.getTagIds() != null && !problemDTO.getTagIds().isEmpty()){
            List<Integer> tags = tagService.getTagIdByIds(problemDTO.getTagIds());
            int result = problemMapper.saveProblemTags(tags, id);
            if (result != problemDTO.getTagIds().size()){
                throw new BaseException(ProblemConstants.TAG_SAVE_ERROR);
            }
        }
        // 保存代码模板
        if (problemDTO.getCodeTemplate() != null && problemDTO.getCodeTemplate().getLanguageId() != null){
            // 判断语言是否存在
            Language language = problemMapper.getLanguageById(problemDTO.getCodeTemplate().getLanguageId());
            if (language == null){
                throw new BaseException(ProblemConstants.LANGUAGE_NOT_FOUND);
            }
            // 保存模板
            CodeTemplate codeTemplate = new CodeTemplate();
            codeTemplate.setProblemId(id);
            codeTemplate.setLanguageId(problemDTO.getCodeTemplate().getLanguageId());
            codeTemplate.setTemplateCode(problemDTO.getCodeTemplate().getTemplateCode());
            codeTemplateService.saveCodeTemplate(id, problemDTO.getCodeTemplate().getLanguageId(), problemDTO.getCodeTemplate().getTemplateCode());
        }
        problemDTO.getTestcases().forEach(testcase -> {
            testcase.setProblemId(id);
        });
        problemCaseService.insertBatch(problemDTO.getTestcases());
        return id;
    }

    public Integer getProblemCount() {
        return problemMapper.getProblemCount();
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteProblem(List<Integer> ids) {
        // 删除用例
        List<String> deletePaths = problemCaseService.deleteTestCasesByProblemIds(ids);
        int result = problemMapper.deleteProblemBatch(ids);
        if (result != ids.size()){
            throw new BaseException(ProblemConstants.PROBLEM_DELETE_ERROR);
        }
        // 删除代码模板
        codeTemplateService.deleteCodeTemplateByProblemIds(ids);
        // 删除提交记录
        submitService.deleteSubmitByProblemIds(ids);
        // 删除用例文件
        if (deletePaths != null && !deletePaths.isEmpty()){
            deletePaths.forEach(path -> {
                boolean del = FileUtil.del(path);
                if (!del){
                    log.warn("delete file error: {}", path);
                    // todo: 记录日志
                }
            });
        }
    }

    @Transactional
    public void updateProblem(Integer id, String title, String description, String inputDesc,
                              String outputDesc, Long timeLimit, Long memoryLimit, Integer difficulty, Integer auth) {
        if (id == null){
            throw new BaseException(ProblemConstants.PROBLEM_ID_NULL_ERROR);
        }
        Problem problem = problemMapper.getProblemById(id);
        if (problem == null){
            throw new BaseException(ProblemConstants.PROBLEM_NOT_FOUND);
        }
        Problem dbProblem = new Problem();
        dbProblem.setId(id);
        if (StringUtils.isNotBlank(title)) dbProblem.setTitle(title);
        if (StringUtils.isNotBlank(description)) dbProblem.setDescription(description);
        if (StringUtils.isNotBlank(inputDesc)) dbProblem.setInputDesc(inputDesc);
        if (StringUtils.isNotBlank(outputDesc)) dbProblem.setOutputDesc(outputDesc);
        if (timeLimit != null) dbProblem.setTimeLimit(timeLimit);
        if (memoryLimit != null) dbProblem.setMemoryLimit(memoryLimit);
        if (difficulty != null) dbProblem.setDifficulty(difficulty);
        if (auth != null) dbProblem.setAuth(auth);
        problemMapper.update(dbProblem);
    }

    public List<Language> getLanguageList() {
        return problemMapper.getLanguageList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProblemTags(Integer id, List<Integer> tagIds) {
        if (id == null || tagIds == null || tagIds.isEmpty()){
            throw new BaseException(ProblemConstants.PROBLEM_ID_NULL_ERROR);
        }
        // 删除原有标签
        problemMapper.deleteProblemTags(id);
        // 保存新标签
        problemMapper.saveProblemTags(tagIds, id);
    }

    // 公开题目
    public void setAuthPublicByIds(List<Integer> problems) {
        if (problems == null || problems.isEmpty()){
            return;
        }
        problemMapper.setAuthPublicByIds(problems);
    }
}