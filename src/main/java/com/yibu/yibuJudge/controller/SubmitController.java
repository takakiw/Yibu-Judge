package com.yibu.yibuJudge.controller;

import com.github.pagehelper.Page;
import com.yibu.yibuJudge.constant.SubmitConstants;
import com.yibu.yibuJudge.model.dto.SubmitDTO;
import com.yibu.yibuJudge.model.entity.SubmitPage;
import com.yibu.yibuJudge.model.response.JudgeResult;
import com.yibu.yibuJudge.model.response.PageBean;
import com.yibu.yibuJudge.model.response.Result;
import com.yibu.yibuJudge.model.vo.SubmitVO;
import com.yibu.yibuJudge.service.SubmitService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/submit")
@Validated
public class SubmitController {

    private final SubmitService submitService;

    public SubmitController(SubmitService submitService) {
        this.submitService = submitService;
    }

    @GetMapping("/list/{problemId}")
    public Result<PageBean<SubmitPage>> submitStatus(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                     @RequestParam(required = false, defaultValue = "10")  Integer size,
                                                     @PathVariable("problemId") Integer problemId) {
        PageBean<SubmitPage> pageBean = submitService.getSubmitList(page, size, problemId);
        return Result.success(pageBean);
    }

    @GetMapping("/count/{problemId}")
    public Result<Integer> submitCount(@PathVariable("problemId") Integer problemId) {
        Integer count = submitService.getSubmitCount(problemId);
        return Result.success(count);
    }

    @GetMapping("/recent")
    public Result<PageBean<SubmitPage>> recentSubmit(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                     @RequestParam(required = false, defaultValue = "5") Integer size) {
        Page<SubmitPage> pageBean = submitService.getRecentSubmit(page, size);
        return Result.success(new PageBean<>(pageBean.getTotal(), pageBean.getResult()));
    }


    @GetMapping("/{submitId}")
    public Result<SubmitVO> getSubmit(@PathVariable("submitId") Long submitId) {
        SubmitVO submit = submitService.getSubmit(submitId);
        return Result.success(submit);
    }

    @GetMapping("/heatmap")
    public Result<Map<String, Long>> heatmapSubmit(@RequestParam(required = false, defaultValue = "2024") Integer year){
        return Result.success(submitService.getHeatmap(year));
    }

    @PostMapping("/debug")
    public Result<JudgeResult> debugSubmit(@RequestBody @Valid SubmitDTO submitDTO) {
        return submitService.debugSubmit(submitDTO.getLangId(), submitDTO.getCode(), submitDTO.getInputData());
    }

    @PostMapping("/submit")
    public Result<Long> submit(@RequestBody @Valid SubmitDTO submitDTO) {
        if(submitDTO.getProblemId() == null){
            return Result.error(SubmitConstants.PARAM_ERROR);
        }
        Long submitId = submitService.submit(submitDTO.getProblemId(), submitDTO.getLangId(), submitDTO.getCode(), submitDTO.getContestId());
        return Result.success(submitId);
    }

    @PostMapping("/resultApi")
    public Result<SubmitVO> resultApi(@RequestBody SubmitDTO submitDTO) {
        SubmitVO submitVO = submitService.resultApi(submitDTO.getId());
        if(submitVO == null){
            return Result.error(SubmitConstants.SUBMIT_WAITING);
        }
        return Result.success(submitVO);
    }

}