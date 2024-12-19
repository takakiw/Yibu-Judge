package com.yibu.yibujudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubmitHeat {
    private String submitTime;
    private Long submitCount;
}
