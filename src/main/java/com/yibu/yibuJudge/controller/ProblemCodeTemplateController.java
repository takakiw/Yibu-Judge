package com.yibu.yibuJudge.controller;


import com.yibu.yibuJudge.annotation.CheckAuth;
import com.yibu.yibuJudge.model.dto.CodeTemplateDTO;
import com.yibu.yibuJudge.model.response.Result;
import com.yibu.yibuJudge.model.vo.CodeTemplateVO;
import com.yibu.yibuJudge.service.CodeTemplateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problem/template")
public class ProblemCodeTemplateController {

    private final CodeTemplateService templateService;
    public ProblemCodeTemplateController(CodeTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/template/{id}")
    public Result<CodeTemplateVO> getProblemTemplate(@PathVariable("id") Integer id, @RequestParam(value = "langId", defaultValue = "1") Integer langId) {
        CodeTemplateVO templateVO = templateService.getProblemCodeTemplate(id, langId);
        return Result.success(templateVO);
    }

    @CheckAuth
    @PostMapping("/template")
    public Result<Void> saveProblemTemplate(@RequestBody CodeTemplateDTO codeTemplateDTO) {
        templateService.saveCodeTemplate(codeTemplateDTO.getProblemId(), codeTemplateDTO.getLanguageId(), codeTemplateDTO.getTemplateCode());
        return Result.success();
    }

    @CheckAuth
    @PutMapping("/template")
    public Result<Void> updateProblemTemplate(@RequestBody CodeTemplateDTO codeTemplateDTO) {
        templateService.updateCodeTemplate(codeTemplateDTO.getId(), codeTemplateDTO.getLanguageId(), codeTemplateDTO.getTemplateCode());
        return Result.success();
    }

    @CheckAuth
    @DeleteMapping("/template/{id}")
    public Result<Void> deleteProblemTemplate(@PathVariable("id") Integer id) {
        templateService.deleteCodeTemplate(id);
        return Result.success();
    }

}
