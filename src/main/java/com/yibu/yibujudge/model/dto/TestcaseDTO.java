package com.yibu.yibujudge.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestcaseDTO {
    private Long id;
    private Integer problemId;
    @NotBlank(message = "输入数据不能为空")
    private String inputData;
    @NotNull(message = "输出数据不能为空")
    private String inputDesc;
    @NotBlank(message = "输出数据不能为空")
    private String outputData;
    @NotNull(message = "输出描述不能为空")
    private String outputDesc;
    List<Integer> tagIds;
}
