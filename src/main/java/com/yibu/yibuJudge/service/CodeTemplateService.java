package com.yibu.yibuJudge.service;

import cn.hutool.core.bean.BeanUtil;
import com.yibu.yibuJudge.constant.ProblemConstants;
import com.yibu.yibuJudge.exceptions.BaseException;
import com.yibu.yibuJudge.mapper.CodeTemplateMapper;
import com.yibu.yibuJudge.model.entity.CodeTemplate;
import com.yibu.yibuJudge.model.vo.CodeTemplateVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodeTemplateService {

    private final CodeTemplateMapper templateMapper;
    public CodeTemplateService(CodeTemplateMapper templateMapper) {
        this.templateMapper = templateMapper;
    }


    public CodeTemplateVO getProblemCodeTemplate(Integer problemId, Integer langId) {
        if (problemId == null || langId == null){
            throw new BaseException(ProblemConstants.PARAM_ERROR);
        }
        CodeTemplate codeTemplate = templateMapper.getProblemCodeTemplateByProblemId(problemId, langId);
        CodeTemplateVO codeTemplateVO = BeanUtil.copyProperties(codeTemplate, CodeTemplateVO.class);
        if (codeTemplateVO!= null) {
            String useLanguage = templateMapper.getLanguageById(langId).getName();
            codeTemplateVO.setLanguage(useLanguage);
        }
        return codeTemplateVO;
    }



    public void saveCodeTemplate(Integer problemId, Integer languageId, String templateCode) {
        if (problemId == null || languageId == null || templateCode == null){
            throw new BaseException(ProblemConstants.PARAM_ERROR);
        }
        // 判断是否存在模板，如果存在报错
        CodeTemplate codeTemplate = templateMapper.getProblemCodeTemplateByProblemId(problemId, languageId);
        if (codeTemplate!= null){
            throw new BaseException(ProblemConstants.CODE_TEMPLATE_ALREADY_EXISTS);
        }
        CodeTemplate dbCodeTemplate = new CodeTemplate();
        dbCodeTemplate.setProblemId(problemId);
        dbCodeTemplate.setLanguageId(languageId);
        dbCodeTemplate.setTemplateCode(templateCode);
        int result = templateMapper.insert(dbCodeTemplate);
        if (result == 0){
            throw new BaseException(ProblemConstants.SAVE_CODE_TEMPLATE_ERROR);
        }
    }

    public void updateCodeTemplate(Integer id, Integer languageId, String templateCode) {
        CodeTemplate codeTemplate = templateMapper.getProblemCodeTemplateById(id);
        if (codeTemplate == null){
            throw new BaseException(ProblemConstants.CODE_TEMPLATE_NOT_EXISTS);
        }
        codeTemplate.setTemplateCode(templateCode);
        codeTemplate.setLanguageId(languageId);
        int result = templateMapper.updateCodeTemplate(codeTemplate);
        if (result == 0){
            throw new BaseException(ProblemConstants.UPDATE_CODE_TEMPLATE_ERROR);
        }
    }

    public void deleteCodeTemplate(Integer id) {
        templateMapper.deleteById(id);
    }

    public void deleteCodeTemplateByProblemIds(List<Integer> problemIds) {
        if (problemIds == null || problemIds.isEmpty()){
            return;
        }
        templateMapper.deleteByProblemIds(problemIds);
    }
}
