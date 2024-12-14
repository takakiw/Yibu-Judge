package com.yibu.yibuJudge.controller;

import com.yibu.yibuJudge.model.dto.TestcaseDTO;
import com.yibu.yibuJudge.model.entity.Testcase;
import com.yibu.yibuJudge.model.response.Result;
import com.yibu.yibuJudge.model.vo.TestcaseVO;
import com.yibu.yibuJudge.service.ProblemCaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem/testcase")
public class ProblemTestcaseController {

    private final ProblemCaseService problemCaseService;

    public ProblemTestcaseController(ProblemCaseService problemCaseService) {
        this.problemCaseService = problemCaseService;
    }

    // todo : 检查是否有权限
    @GetMapping("/list/{problemId}")
    public Result<List<Testcase>> list(@PathVariable("problemId") Integer problemId) {
        return Result.success(problemCaseService.getTestCasesByProblemId(problemId, -1));
    }

    // todo : 检查是否有权限
    @GetMapping("/{id}")
    public Result<TestcaseVO> getTestcase(@PathVariable("id") Long id){
        return Result.success(problemCaseService.getTestcaseById(id));
    }

    // todo : 检查是否有权限
    @PostMapping("/add")
    public Result<Void> add(@RequestBody List<TestcaseDTO> testcases) {
        problemCaseService.insertBatch(testcases);
        return Result.success();
    }

    // todo : 检查是否有权限
    @DeleteMapping("/{ids}")
    public Result<Void> delete(@PathVariable("ids") List<Long> ids) {
        problemCaseService.deleteTestCasesByIds(ids);
        return Result.success();
    }

    // todo : 检查是否有权限
    @PutMapping("/update")
    public Result<Void> update(@RequestBody TestcaseDTO testcaseDTO) {
        problemCaseService.updateTestcase(testcaseDTO.getId(),
                testcaseDTO.getInputDesc(), testcaseDTO.getOutputDesc(),
                testcaseDTO.getInputData(), testcaseDTO.getOutputData());
        return Result.success();
    }
}
