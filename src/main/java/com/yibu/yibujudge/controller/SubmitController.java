package com.yibu.yibujudge.controller;

import com.github.pagehelper.Page;
import com.yibu.yibujudge.constant.SubmitConstants;
import com.yibu.yibujudge.model.dto.SubmitDTO;
import com.yibu.yibujudge.model.entity.SubmitPage;
import com.yibu.yibujudge.model.response.JudgeResult;
import com.yibu.yibujudge.model.response.PageBean;
import com.yibu.yibujudge.model.response.Result;
import com.yibu.yibujudge.model.vo.SubmitCountVO;
import com.yibu.yibujudge.model.vo.SubmitVO;
import com.yibu.yibujudge.service.SubmitService;
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
    public Result<PageBean<SubmitPage>> submitList(@RequestParam(required = false, defaultValue = "1") Integer page,
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
    public Result<Map<String, Long>> heatmapSubmit(@RequestParam(required = false, defaultValue = "2024") Integer year) {
        return Result.success(submitService.getHeatmap(year));
    }

    @PostMapping("/debug")
    public Result<JudgeResult> debugSubmit(@RequestBody @Valid SubmitDTO submitDTO) {
        return submitService.debugSubmit(submitDTO.getLangId(), submitDTO.getCode(), submitDTO.getInputData());
    }

    @PostMapping("/submit")
    public Result<Long> submit(@RequestBody @Valid SubmitDTO submitDTO) {
        if (submitDTO.getProblemId() == null) {
            return Result.error(SubmitConstants.PARAM_ERROR);
        }
        Long submitId = submitService.submit(submitDTO.getProblemId(), submitDTO.getLangId(), submitDTO.getCode(), submitDTO.getContestId());
        return Result.success(submitId);
    }

    @PostMapping("/resultApi")
    public Result<SubmitVO> resultApi(@RequestBody SubmitDTO submitDTO) {
        SubmitVO submitVO = submitService.resultApi(submitDTO.getId());
        if (submitVO == null) {
            SubmitVO nullSubmitVO = new SubmitVO();
            nullSubmitVO.setStatus(0);
            return Result.success(nullSubmitVO);
        }
        return Result.success(submitVO);
    }


    /**
     * 获取用户提交记录数量(各个难度的通过数)
     * @param userId
     * @return
     */
    @GetMapping("/submit/count/{userId}")
    public Result<SubmitCountVO> submitCountByUser(@PathVariable("userId") Long userId) {
        SubmitCountVO submitCountVO = submitService.getSubmitCountByUser(userId);
        return Result.success(submitCountVO);
    }
}