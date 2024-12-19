package com.yibu.yibujudge.model.mq;

import com.yibu.yibujudge.model.response.JudgeResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResultMQType {

    private Long submitId; // 提交ID
    private Integer contestId; // 比赛ID
    private Integer problemId; // 题目ID
    private Long userId; // 用户ID
    private JudgeResult judgeResult; // 判题结果
}
