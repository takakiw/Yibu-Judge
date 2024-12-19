package com.yibu.yibujudge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeTemplateDTO {
    private Integer id;
    private Integer problemId;
    private Integer languageId;
    private String templateCode;
}
