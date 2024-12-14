package com.yibu.yibuJudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestProblems implements Serializable {
    private Integer id; // 主键
    private String title; // 题目标题
    private Integer contestId; // 竞赛ID
    private Integer problemId; // 题目ID
    private Integer problemOrder; // 题目顺序
    private Integer score; // 题目分值
    private LocalDateTime createTime; // 创建时间
}