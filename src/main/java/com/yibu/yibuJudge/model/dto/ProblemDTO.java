package com.yibu.yibuJudge.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemDTO implements Serializable {
    private Integer id;
    @NotBlank(message = "标题不能为空")
    @Size(max = 50, message = "标题长度不能超过50个字符")
    private String title;
    @NotBlank(message = "描述不能为空")
    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;
    @NotBlank(message = "输入描述不能为空")
    @Size(max = 200, message = "输入描述长度不能超过200个字符")
    private String inputDesc;
    @NotBlank(message = "输出描述不能为空")
    @Size(max = 200, message = "输出描述长度不能超过200个字符")
    private String outputDesc;
    @NotNull(message = "时间限制不能为空")
    private Long timeLimit;
    @NotNull(message = "内存限制不能为空")
    private Long memoryLimit;
    @NotNull(message = "难度不能为空")
    private Integer difficulty;
    @NotNull(message = "题目类型不能为空")
    private Integer auth;
    @NotBlank(message = "数据范围不能为空")
    @Size(max = 200, message = "数据范围长度不能超过200个字符")
    private String dataRange;
    @NotNull(message = "测试用例不能为空")
    List<TestcaseDTO> testcases;
    private List<Integer> tagIds;
    private CodeTemplateDTO codeTemplate;
    private Integer score; // 题目分值, contest中使用
    private Integer order; // 题目顺序, contest中使用
}
