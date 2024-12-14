package com.yibu.yibuJudge.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.yibu.yibuJudge.constant.ProblemConstants;
import com.yibu.yibuJudge.exceptions.BaseException;
import com.yibu.yibuJudge.mapper.ProblemCaseMapper;
import com.yibu.yibuJudge.model.dto.TestcaseDTO;
import com.yibu.yibuJudge.model.entity.Testcase;
import com.yibu.yibuJudge.model.vo.TestcaseVO;
import com.yibu.yibuJudge.properties.FileProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProblemCaseService {

    private final ProblemCaseMapper problemCaseMapper;
    private final FileProperties fileProperties;
    public ProblemCaseService(ProblemCaseMapper problemCaseMapper,
                              FileProperties fileProperties) {
        this.problemCaseMapper = problemCaseMapper;
        this.fileProperties = fileProperties;
    }




    public List<Testcase> getTestCasesByProblemId(Integer id, int limit) {
        return problemCaseMapper.getTestCasesByProblemId(id, limit);
    }

    public void insertBatch(List<TestcaseDTO> testcases) {
        if (testcases == null || testcases.isEmpty()){
            return;
        }
        List<Testcase> testcaseList = new ArrayList<>();
        int i = 0;
        Integer problemId = testcases.get(0).getProblemId();
        if (problemId == null){
            throw new BaseException(ProblemConstants.TESTCASE_PROBLEM_ID_NULL_ERROR);
        }
        String inputDir = fileProperties.getCaseInPath() + "/" + problemId;
        String outputDir = fileProperties.getCaseOutPath() + "/" + problemId;
        FileUtil.mkdir(inputDir);
        FileUtil.mkdir(outputDir);
        try {
            for (i = 0; i < testcases.size(); i++) {
                TestcaseDTO testcaseDTO = testcases.get(i);
                if (testcaseDTO.getProblemId() == null){
                    throw new BaseException(ProblemConstants.TESTCASE_PROBLEM_ID_NULL_ERROR);
                }
                Testcase testcase = new Testcase();
                testcase.setProblemId(testcaseDTO.getProblemId());
                testcase.setInputDesc(testcaseDTO.getInputDesc());
                testcase.setOutputDesc(testcaseDTO.getOutputDesc());
                String index = UUID.randomUUID().toString().replace("-", "");
                try(FileOutputStream inOutputStream = new FileOutputStream( inputDir + "/" + index + ".txt");
                    FileOutputStream OutOutputStream = new FileOutputStream( outputDir + "/" + index + ".txt")){
                    IoUtil.writeUtf8(inOutputStream, true, testcaseDTO.getInputData());
                    IoUtil.writeUtf8(OutOutputStream, true, testcaseDTO.getOutputData());
                }catch (Exception e){
                    throw new BaseException(ProblemConstants.TESTCASE_SAVE_ERROR);
                }
                testcase.setInputPath(inputDir + "/" + index + ".txt");
                testcase.setOutputPath(outputDir + "/" + index + ".txt");
                testcaseList.add(testcase);
            }
            int result = problemCaseMapper.insertBatch(testcaseList);
            if (result != testcaseList.size()){
                throw new BaseException(ProblemConstants.TESTCASE_SAVE_ERROR);
            }
        } catch (Exception e){
            FileUtil.del(inputDir);
            FileUtil.del(outputDir);
            throw new BaseException(ProblemConstants.TESTCASE_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> deleteTestCasesByProblemIds(List<Integer> problemIds) {
        if (problemIds == null || problemIds.isEmpty()) {
            return null;
        }
        // 查询出所有测试用例
        List<Testcase> testcaseList = problemCaseMapper.getTestCasesByProblemIdBatch(problemIds);
        if (testcaseList == null || testcaseList.isEmpty()) {
            return null;
        }
        // 提取文件路径
        List<String> deletePaths = new ArrayList<>();
        for (Testcase testcase : testcaseList) {
            deletePaths.add(testcase.getInputPath());
            deletePaths.add(testcase.getOutputPath());
        }

        int result = problemCaseMapper.deleteTestCasesByProblemIds(problemIds);
        if (result != testcaseList.size()) {
            throw new BaseException(ProblemConstants.TESTCASE_DELETE_ERROR);
        }
        return deletePaths;
    }

    public TestcaseVO getTestcaseById(Long id) {
        Testcase testcase = problemCaseMapper.getTestcaseById(id);
        if (testcase == null) {
            return new TestcaseVO();
        }
        TestcaseVO testcaseVO = BeanUtil.copyProperties(testcase, TestcaseVO.class);
        try(FileInputStream inInputStream = new FileInputStream(testcase.getInputPath());
            FileInputStream outInputStream = new FileInputStream(testcase.getOutputPath())){
            String inputData = IoUtil.readUtf8(inInputStream);
            String outputData = IoUtil.readUtf8(outInputStream);
            testcaseVO.setInputData(inputData);
            testcaseVO.setOutputData(outputData);
        }catch (Exception e){
            throw new BaseException(ProblemConstants.TESTCASE_READ_ERROR);
        }
        return testcaseVO;
    }


    public void deleteTestCasesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        int result = problemCaseMapper.deleteTestCasesByIds(ids);
        if (result != ids.size()) {
            throw new BaseException(ProblemConstants.TESTCASE_DELETE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTestcase(Long id, String inputDesc, String outputDesc, String inputData, String outputData) {
        if (id == null){
            throw new BaseException(ProblemConstants.TESTCASE_ID_NULL_ERROR);
        }
        Testcase testcase = problemCaseMapper.getTestcaseById(id);
        if (testcase == null){
            throw new BaseException(ProblemConstants.TESTCASE_NOT_EXIST_ERROR);
        }
        Testcase dbTestcase = new Testcase();
        dbTestcase.setId(id);
        if (StringUtils.isNotBlank(inputDesc)) dbTestcase.setInputDesc(inputDesc);
        if (StringUtils.isNotBlank(outputDesc)) dbTestcase.setOutputDesc(outputDesc);
        problemCaseMapper.update(dbTestcase);
        if (inputData != null){
            try(FileOutputStream inOutputStream = new FileOutputStream( testcase.getInputPath());){
                IoUtil.writeUtf8(inOutputStream, true, inputData);
            }catch (Exception e){
                throw new BaseException(ProblemConstants.TESTCASE_SAVE_ERROR);
            }
        }
        if (outputData != null){
            try(FileOutputStream OutOutputStream = new FileOutputStream( testcase.getOutputPath());){
                IoUtil.writeUtf8(OutOutputStream, true, outputData);
            }catch (Exception e){
                throw new BaseException(ProblemConstants.TESTCASE_SAVE_ERROR);
            }
        }
    }
}