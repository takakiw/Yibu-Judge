package com.yibu.yibujudge.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitDTO {
    private Long id;
    private Integer problemId;
    @NotNull(message = "语言ID不能为空")
    private Integer langId;
    @NotNull(message = "代码不能为空")
    private String code;
    private String inputData;
    private Integer contestId;
}
