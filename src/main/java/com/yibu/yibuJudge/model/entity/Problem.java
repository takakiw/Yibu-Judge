package com.yibu.yibuJudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Problem implements Serializable {


    private Integer id;

    private String title;

    private String description;

    private String inputDesc;

    private String outputDesc;

    private Long timeLimit;

    private Long memoryLimit;

    private Integer difficulty;

    private Integer submitCount;

    private Integer acceptedCount;

    private Integer auth;

    private String dataRange;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String tags;
}
