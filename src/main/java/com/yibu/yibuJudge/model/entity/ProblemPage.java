package com.yibu.yibuJudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemPage implements Serializable {

    private Integer id;
    private String title;
    private Integer difficulty;
    private Integer submitCount;
    private Integer acceptedCount;
    private Integer status; // 0: 未开始，1：ac，2：尝试过
}
