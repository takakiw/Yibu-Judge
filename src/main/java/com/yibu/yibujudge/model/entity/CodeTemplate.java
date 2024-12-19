package com.yibu.yibujudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeTemplate {
    private Integer id;
    private Integer problemId;
    private Integer languageId;
    private String templateCode;
}
