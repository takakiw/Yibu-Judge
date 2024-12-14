package com.yibu.yibuJudge.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitVO {
    private Long id;
    private Long userId;
    private Long problemId;
    private Integer status;
    private String languageName;
    private Integer runtime;
    private Integer memory;
    private String code;
    private String resultMessage;
    private LocalDateTime submitTime;
}
