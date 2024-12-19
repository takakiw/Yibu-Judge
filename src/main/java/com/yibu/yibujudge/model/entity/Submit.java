package com.yibu.yibujudge.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submit implements Serializable {
    private Long id;
    private Long userId;
    private Integer problemId;
    private Integer contestId;
    private Integer status;
    private Integer langId;
    // 非数据库字段，用于前端显示
    private String languageName;
    private Long runtime;
    private Long memory;
    private String codePath;
    private String resultMessage;
    private LocalDateTime submitTime;
}
