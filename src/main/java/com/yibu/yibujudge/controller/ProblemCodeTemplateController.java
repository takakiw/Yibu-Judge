package com.yibu.yibujudge.controller;


import com.yibu.yibujudge.annotation.CheckAuth;
import com.yibu.yibujudge.model.dto.CodeTemplateDTO;
import com.yibu.yibujudge.model.response.Result;
import com.yibu.yibujudge.model.vo.CodeTemplateVO;
import com.yibu.yibujudge.service.CodeTemplateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problem/template")
public class ProblemCodeTemplateController {

    private final CodeTemplateService templateService;
    public ProblemCodeTemplateController(CodeTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/{problemId}")
    public Result<CodeTemplateVO> getProblemTemplate(@PathVariable("problemId") Integer id,
                                                     @RequestParam(value = "langId", defaultValue = "1") Integer langId) {
        CodeTemplateVO templateVO = templateService.getProblemCodeTemplate(id, langId);
        return Result.success(templateVO);
    }

    @CheckAuth
    @PostMapping("/save")
    public Result<Void> saveProblemTemplate(@RequestBody CodeTemplateDTO codeTemplateDTO) {
        templateService.saveCodeTemplate(codeTemplateDTO.getProblemId(), codeTemplateDTO.getLanguageId(), codeTemplateDTO.getTemplateCode());
        return Result.success();
    }

    @CheckAuth
    @PutMapping("/update")
    public Result<Void> updateProblemTemplate(@RequestBody CodeTemplateDTO codeTemplateDTO) {
        templateService.updateCodeTemplate(codeTemplateDTO.getId(), codeTemplateDTO.getLanguageId(), codeTemplateDTO.getTemplateCode());
        return Result.success();
    }

    @CheckAuth
    @DeleteMapping("/{id}")
    public Result<Void> deleteProblemTemplate(@PathVariable("id") Integer id) {
        templateService.deleteCodeTemplate(id);
        return Result.success();
    }

}
