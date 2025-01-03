package com.yibu.yibujudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Testcase implements Serializable {
    private Long id;
    private Integer problemId;
    private String inputPath;
    private String inputDesc;
    private String outputPath;
    private String outputDesc;
}