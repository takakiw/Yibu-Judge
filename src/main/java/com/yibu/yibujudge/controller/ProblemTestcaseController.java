package com.yibu.yibujudge.controller;

import com.yibu.yibujudge.annotation.CheckAuth;
import com.yibu.yibujudge.model.dto.TestcaseDTO;
import com.yibu.yibujudge.model.entity.Testcase;
import com.yibu.yibujudge.model.response.Result;
import com.yibu.yibujudge.model.vo.TestcaseVO;
import com.yibu.yibujudge.service.ProblemCaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem/testcase")
public class ProblemTestcaseController {

    private final ProblemCaseService problemCaseService;

    public ProblemTestcaseController(ProblemCaseService problemCaseService) {
        this.problemCaseService = problemCaseService;
    }

    @CheckAuth
    @GetMapping("/list/{problemId}")
    public Result<List<Testcase>> list(@PathVariable("problemId") Integer problemId) {
        return Result.success(problemCaseService.getTestCasesByProblemId(problemId, -1));
    }

    @CheckAuth
    @GetMapping("/{id}")
    public Result<TestcaseVO> getTestcase(@PathVariable("id") Long id) {
        return Result.success(problemCaseService.getTestcaseById(id));
    }

    @CheckAuth
    @PostMapping("/add")
    public Result<Void> add(@RequestBody List<TestcaseDTO> testcases) {
        problemCaseService.insertBatch(testcases);
        return Result.success();
    }

    @CheckAuth
    @DeleteMapping("/{ids}")
    public Result<Void> delete(@PathVariable("ids") List<Long> ids) {
        problemCaseService.deleteTestCasesByIds(ids);
        return Result.success();
    }

    @CheckAuth
    @PutMapping("/update")
    public Result<Void> update(@RequestBody TestcaseDTO testcaseDTO) {
        problemCaseService.updateTestcase(testcaseDTO.getId(),
                testcaseDTO.getInputDesc(), testcaseDTO.getOutputDesc(),
                testcaseDTO.getInputData(), testcaseDTO.getOutputData());
        return Result.success();
    }
}
