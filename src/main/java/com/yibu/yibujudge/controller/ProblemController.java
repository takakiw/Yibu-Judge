package com.yibu.yibujudge.controller;

import com.github.pagehelper.Page;
import com.yibu.yibujudge.annotation.CheckAuth;
import com.yibu.yibujudge.model.dto.ProblemDTO;
import com.yibu.yibujudge.model.entity.Language;
import com.yibu.yibujudge.model.entity.ProblemPage;
import com.yibu.yibujudge.model.response.PageBean;
import com.yibu.yibujudge.model.response.Result;
import com.yibu.yibujudge.model.vo.ProblemVO;
import com.yibu.yibujudge.service.ProblemService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem")
@Validated
public class ProblemController {

    private final ProblemService problemService;
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("/list")
    public Result<PageBean<ProblemPage>> getProblemList(
                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  @RequestParam(value = "title", required = false) String title,
                                  @RequestParam(value = "tags", required = false) List<String> tags,
                                  @RequestParam(value = "difficulty", required = false) Integer difficulty,
                                  @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort,
                                  @RequestParam(value = "order", required = false, defaultValue = "id") String order) {
        Page<ProblemPage> pageList = problemService.getProblemList(page, size, tags, title, difficulty,order, sort);
        return Result.success(new PageBean<>(pageList.getTotal(), pageList.getResult()));
    }

    @GetMapping("count")
    public Result<Integer> getProblemCount() {
        return Result.success(problemService.getProblemCount());
    }


    @GetMapping("/{id}")
    public Result<ProblemVO> getProblem(@PathVariable("id") Integer id) {
        ProblemVO problemVO = problemService.getProblem(id);
        return Result.success(problemVO);
    }

    @CheckAuth
    @PostMapping("/add")
    public Result<Void> addProblem(@RequestBody @Valid ProblemDTO problemDTO) {
        problemService.addProblem(problemDTO);
        return Result.success();
    }

    @CheckAuth
    @DeleteMapping("/{ids}")
    public Result<Void> deleteProblem(@PathVariable("ids") List<Integer> ids) {
        problemService.deleteProblem(ids);
        return Result.success();
    }

    @CheckAuth
    @PutMapping("/update")
    public Result<Void> updateProblem(@RequestBody ProblemDTO problemDTO) {
        problemService.updateProblem(problemDTO.getId(),
                                     problemDTO.getTitle(),
                                     problemDTO.getDescription(),
                                     problemDTO.getInputDesc(),
                                     problemDTO.getOutputDesc(),
                                     problemDTO.getTimeLimit(),
                                     problemDTO.getMemoryLimit(),
                                     problemDTO.getDifficulty(),
                                     problemDTO.getAuth());
        return Result.success();
    }

    @CheckAuth
    @PutMapping("/update/tags")
    public Result<Void> updateProblemTags(@RequestBody ProblemDTO problemDTO) {
        problemService.updateProblemTags(problemDTO.getId(), problemDTO.getTagIds());
        return Result.success();
    }

    @GetMapping("/lang/list")
    public Result<List<Language>> getLanguageList() {
        return Result.success(problemService.getLanguageList());
    }
}