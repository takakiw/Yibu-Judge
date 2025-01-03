package com.yibu.yibujudge.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult implements Serializable {
    private int status;
    private String message;
    private String output;

    private String codePath;

    private Long cpuTime;
    private Long memory;
    private Long caseCount;
    private Long acCaseCount;
    private String firstErrorCaseInput; // 第一个错误的测试用例的输入
    private String firstErrorYueOutput; // 第一个错误的测试用例的期望输出
}
