package com.yibu.yibuJudge.model.vo;

import com.yibu.yibuJudge.model.entity.ContestProblems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestVO {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String organizer;
    List<ContestProblems> contestProblems;
    private Boolean register;
}
