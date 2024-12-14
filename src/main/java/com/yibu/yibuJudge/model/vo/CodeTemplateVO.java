package com.yibu.yibuJudge.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeTemplateVO {
    private Integer problemId;
    private String language;
    private String templateCode;
}