package com.yibu.yibujudge.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitCountVO {
    private int acCount;
    private Map<Integer, Long> submitCount;
}
