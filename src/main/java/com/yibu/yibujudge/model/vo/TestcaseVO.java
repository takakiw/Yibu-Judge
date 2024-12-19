package com.yibu.yibujudge.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestcaseVO {
    private Long id;
    private Integer problemId;
    private String inputData;
    private String inputDesc;
    private String outputData;
    private String outputDesc;
}