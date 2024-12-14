package com.yibu.yibuJudge.model.mq;

import com.yibu.yibuJudge.model.entity.Problem;
import com.yibu.yibuJudge.model.entity.Testcase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitMQType {
    private Long submitId; // 用于唯一标识提交
    private Problem problem; // 题目信息
    private String language; // 语言
    private String code; // 代码
    private Long userId; // 用户ID
    private Integer contestId; // 比赛ID
    private List<Testcase> testcases; // 测试用例
}