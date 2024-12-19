package com.yibu.yibujudge.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemVO {
    private Integer id;
    private String title;
    private String description;
    private Integer difficulty;
    private String inputDesc;
    private String outputDesc;
    private Long timeLimit;
    private Long memoryLimit;
    private Integer status; // 0:未开始， 1:ac 2:尝试过但未通过 3:wa 4:tle 5:mle 6:rte 7:ce 8:pending 9:judging
    private String dataRange;
    List<TestcaseVO> testcases; // 默认2个测试用例
    private String tags;
}
