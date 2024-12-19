package com.yibu.yibujudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Testcase {
    private Long id;
    private Integer problemId;
    private String inputPath;
    private String inputDesc;
    private String outputPath;
    private String outputDesc;
}