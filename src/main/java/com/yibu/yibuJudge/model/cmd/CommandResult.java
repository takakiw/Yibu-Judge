package com.yibu.yibuJudge.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandResult {
    private Long cpuTime;
    private Long realTime;
    private Long memory;
    private Integer signal;
    private Integer exitCode;
    private Integer error;
    private Integer result;
    private String OutputPath;
    private String ErrorPath;
}
