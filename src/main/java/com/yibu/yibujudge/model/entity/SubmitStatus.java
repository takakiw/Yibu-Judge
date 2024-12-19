package com.yibu.yibujudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitStatus {
    private Integer problemId;         // 题目 ID
    private Integer totalSubmissions;  // 提交总数
    private Integer acCount;           // AC 的数量
}