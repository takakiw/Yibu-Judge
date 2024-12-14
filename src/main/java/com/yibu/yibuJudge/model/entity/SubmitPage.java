package com.yibu.yibuJudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitPage {
    private Long id;
    private Integer status;
    // 非数据库字段，用于前端显示
    private String languageName;
    private Integer runtime;
    private Integer memory;
    private LocalDateTime submitTime;
}