package com.yibu.yibujudge.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private Integer id;
    @NotNull(message = "标签名称不能为空")
    @Size(min = 2, max = 10, message = "标签名称长度必须在2-10之间")
    private String name;
}
